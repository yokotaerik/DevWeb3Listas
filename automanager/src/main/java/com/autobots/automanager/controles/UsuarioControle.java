package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkUsuario;
import com.autobots.automanager.modelos.atualizador.UsuarioAtualizador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioControle {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private AdicionadorLinkUsuario adicionadorLinkUsuario;

	@Autowired
	private UsuarioAtualizador atualizador;

	@GetMapping("get/unique/{id}")
	public ResponseEntity<?> obterUsuario(@PathVariable long id)
	{
		var usuario = usuarioRepositorio.getById(id);
		if(usuario == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		} else {
			adicionadorLinkUsuario.adicionarLink(usuario);

            return ResponseEntity.status(HttpStatus.OK).body(usuario);
		}
	}

	@GetMapping("get/all")
	public ResponseEntity<?> obterUsuarios() {
		List<Usuario> usuarios = usuarioRepositorio.findAll();
		if(usuarios.isEmpty()){
			return ResponseEntity.notFound().build();
		} else {
			adicionadorLinkUsuario.adicionarLink(usuarios);
			return ResponseEntity.ok(usuarios);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
		usuarioRepositorio.save(usuario);

		adicionadorLinkUsuario.adicionarLink(usuario);

		return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarUsuario(@RequestBody Usuario usuario) {
		Usuario usuarioDb = usuarioRepositorio.getById(usuario.getId());
		if(usuario == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		}
		atualizador.atualizar(usuarioDb, usuario);
		usuarioRepositorio.save(usuarioDb);
		adicionadorLinkUsuario.adicionarLink(usuarioDb);
		return ResponseEntity.status(HttpStatus.OK).body(usuarioDb);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirUsuario(@PathVariable Long id) {
		try {
			usuarioRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Usuario excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir usuario");
		}
	}
}
