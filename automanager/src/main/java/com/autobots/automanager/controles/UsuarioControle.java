package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.usuario.LoginDTO;
import com.autobots.automanager.dtos.usuario.UsuarioDTO;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.jwt.ProvedorJwt;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkUsuario;
import com.autobots.automanager.modelos.atualizador.UsuarioAtualizador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private AdicionadorLinkUsuario adicionadorLinkUsuario;

	@Autowired
	private UsuarioAtualizador atualizador;

	@Autowired
	private ProvedorJwt provedorJwt;

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDTO data){
		var usuario = usuarioRepositorio.findyByCredencialNomeUsuario(data.getNomeUsuario());
		if(usuario.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		}
		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
		if(codificador.matches(data.getSenha(), usuario.get().getCredencial().getSenha())){
			var jwt = provedorJwt.proverJwt(usuario.get().getCredencial().getNomeUsuario());
			return ResponseEntity.status(HttpStatus.OK).body(jwt);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta");
		}
	}


	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
	@GetMapping("get/logado")
	public ResponseEntity<?> obterUsuarioLogado() {
		var usuario = authService.obterUsuarioLogado();

		if(usuario == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		} else {
			var dto = UsuarioDTO.from(usuario);
			adicionadorLinkUsuario.adicionarLink(dto);
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("get/unique/{id}")
	public ResponseEntity<?> obterUsuario(@PathVariable long id)
	{
		var usuario = usuarioRepositorio.findById(id).orElse(null);
		if(usuario == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		} else {
			var validacao = verificarPermissoes(authService.obterUsuarioLogado(), usuario.getPerfis())
			if(validacao != null) return validacao;

			var dto = UsuarioDTO.from(usuario);
			adicionadorLinkUsuario.adicionarLink(dto);
            return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("get/all")
	public ResponseEntity<?> obterUsuarios() {
		List<Usuario> usuarios = usuarioRepositorio.findAll();

		var userLogado = authService.obterUsuarioLogado();
		if(!userLogado.getPerfis().contains(Perfil.ROLE_ADMIN)){
			usuarios.removeIf(usuario -> usuario.getPerfis().contains(Perfil.ROLE_ADMIN));
		}
		if(!userLogado.getPerfis().contains(Perfil.ROLE_GERENTE)){
			usuarios.removeIf(usuario -> usuario.getPerfis().contains(Perfil.ROLE_GERENTE));
		}
		if(userLogado.getPerfis().contains(Perfil.ROLE_VENDEDOR) && userLogado.getPerfis().size() == 1){
			usuarios.removeIf(usuario -> usuario.getPerfis().contains(Perfil.ROLE_VENDEDOR));
		}

		if(usuarios.isEmpty()){
			return ResponseEntity.notFound().build();
		} else {
			var dto = UsuarioDTO.from(usuarios);
			adicionadorLinkUsuario.adicionarLink(dto);
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {

		var autor = authService.obterUsuarioLogado();

		var validacao = verificarPermissoes(autor, usuario.getPerfis());
		if(validacao != null) return validacao;

		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
		var nomeUsuarioExiste = usuarioRepositorio.findyByCredencialNomeUsuario(usuario.getCredencial().getNomeUsuario());

		if(nomeUsuarioExiste.isPresent()){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credencial já cadastrada");
		}

		usuario.getCredencial().setSenha(codificador.encode(usuario.getCredencial().getSenha()));

		usuarioRepositorio.save(usuario);

		var dto = UsuarioDTO.from(usuario);
		adicionadorLinkUsuario.adicionarLink(dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario usuario) {
		var usuarioDb = usuarioRepositorio.findById(usuario.getId()).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

		var autor = authService.obterUsuarioLogado();

		var validacao = verificarPermissoes(autor, usuarioDb.getPerfis());
		if(validacao != null) return validacao;

		if (!usuario.getCredencial().getNomeUsuario().isEmpty()) {
			var nomeUsuarioExiste = usuarioRepositorio.findyByCredencialNomeUsuario(usuario.getCredencial().getNomeUsuario());
			if(nomeUsuarioExiste.isPresent()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Credencial já cadastrada");
		}

		atualizador.atualizar(usuarioDb, usuario);
		usuarioRepositorio.save(usuarioDb);
		var dto = UsuarioDTO.from(usuario);
		adicionadorLinkUsuario.adicionarLink(dto);
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
		try {
			var usuario = usuarioRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

			var autor = authService.obterUsuarioLogado();

			var validacao = verificarPermissoes(autor, usuario.getPerfis());
			if(validacao != null) return validacao;

			usuarioRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Usuario excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir usuario");
		}
	}

	public ResponseEntity<String> verificarPermissoes(Usuario autor, Set<Perfil> perfis) {
		if (perfis.contains(Perfil.ROLE_ADMIN) && !autor.getPerfis().contains(Perfil.ROLE_ADMIN)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário não tem permissão para realizar ações de administradores");
		}

		if (perfis.contains(Perfil.ROLE_GERENTE) && !autor.getPerfis().contains(Perfil.ROLE_GERENTE)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário não tem permissão para realizar ações de gerentes");
		}

		if (perfis.contains(Perfil.ROLE_VENDEDOR) && !autor.getPerfis().contains(Perfil.ROLE_GERENTE)) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário não tem permissão para realizar ações de vendedores");
		}

		return null;
	}
}
