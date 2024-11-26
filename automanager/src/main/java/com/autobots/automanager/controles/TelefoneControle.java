package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.telefone.CadastroTelefoneDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.atualizador.TelefoneAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepostorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/telefones")
public class TelefoneControle {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private TelefoneRepostorio telefoneRepositorio;
	@Autowired
	private AdicionadorLinkTelefone adicionadorLinkTelefone;
	@Autowired
	private EmpresaRepositorio empresaRepositorio;
	@Autowired
	private AuthService authService;

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("empresaRepositorio/{id}")
	public ResponseEntity<?> obterTelefone(@PathVariable long id) {
		var telefone = telefoneRepositorio.findById(id).orElse(null);

		if(telefone != null){

			var autor = authService.obterUsuarioLogado();
			var validacao = verificarPermissao(autor, telefone);
			if (validacao != null) return validacao;

			var adicionadorLinkTelefone = new AdicionadorLinkTelefone();
			adicionadorLinkTelefone.adicionarLink(telefone);
			return ResponseEntity.status(HttpStatus.OK).body(telefone);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("get/all")
	public ResponseEntity<?> obterTelefones() {
		List<Telefone> telefones = telefoneRepositorio.findAll();
		if(telefones.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum endereço encontrado");
		} else
		{
			var adicionadorLinkTelefone = new AdicionadorLinkTelefone();
			adicionadorLinkTelefone.adicionarLink(telefones);
			return ResponseEntity.status(HttpStatus.OK).body(telefones);
		}
	}

	@PostMapping("/cadastro/cliente")
	public ResponseEntity<?> cadastrarTelefoneCliente(@RequestBody CadastroTelefoneDTO data){
		Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(data.getClienteId());

		if (usuarioOptional.isPresent()) {

			// Criação do telefone
			var telefone = new Telefone();
			telefone.setDdd(data.getDdd());
			telefone.setNumero(data.getNumero());
			telefone.setUsuario(usuarioOptional.get());

			// Verificação de permissão
			var autor = authService.obterUsuarioLogado();
			var validacao = verificarPermissao(autor, telefone);
			if (validacao != null) return validacao;

			// Atulizando usuario
			Usuario usuario = usuarioOptional.get();
			usuario.getTelefones().add(telefone);

			// Persistencia dos dados
			telefoneRepositorio.save(telefone);
			usuarioRepositorio.save(usuario);
			adicionadorLinkTelefone.adicionarLink(telefone);
			return ResponseEntity.status(HttpStatus.CREATED).body(telefone);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
		}
	}


	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/cadastro/empresa")
	public ResponseEntity<?> cadastrarTelefoneEmpresa(@RequestBody CadastroTelefoneDTO data) {
		Optional<Empresa> empresaOptional = empresaRepositorio.findById(data.getEmpresaId());

		if (empresaOptional.isPresent()) {
			var telefone = new Telefone();
			telefone.setDdd(data.getDdd());
			telefone.setNumero(data.getNumero());
			telefone.setEmpresa(empresaOptional.get());

			Empresa empresa = empresaOptional.get();
			empresa.getTelefones().add(telefone);
			telefoneRepositorio.save(telefone);
			empresaRepositorio.save(empresa);
			adicionadorLinkTelefone.adicionarLink(telefone);
			return ResponseEntity.status(HttpStatus.CREATED).body(telefone);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone telefone) {
		Telefone telefoneDb = telefoneRepositorio.getById(telefone.getId());

		if(telefoneDb == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");

		var autor = authService.obterUsuarioLogado();
		var validacao = verificarPermissao(autor, telefoneDb);
		if (validacao != null) return validacao;

		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefoneDb, telefone);

		telefoneRepositorio.save(telefoneDb);
		adicionadorLinkTelefone.adicionarLink(telefoneDb);

		return ResponseEntity.status(HttpStatus.OK).body(telefoneDb);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirTelefone(@PathVariable Long id) {
		try{
			var telefone = telefoneRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Telefone não encontrado"));

			var autor = authService.obterUsuarioLogado();
			var validacao = verificarPermissao(autor, telefone);
			if (validacao != null) return validacao;

			telefoneRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Endereço excluído com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");
		}
	}


	public ResponseEntity<?> verificarPermissao(Usuario autor, Telefone telefone) {
		if(telefone.getEmpresa() != null && !autor.getPerfis().contains(Perfil.ROLE_ADMIN)){
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar este recurso");
		}

		if(telefone.getUsuario() != null){
			var perfis = telefone.getUsuario().getPerfis();

			if (perfis.contains(Perfil.ROLE_ADMIN) && !autor.getPerfis().contains(Perfil.ROLE_ADMIN)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar este recurso");
			}

			if (perfis.contains(Perfil.ROLE_GERENTE) && !autor.getPerfis().contains(Perfil.ROLE_GERENTE)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar este recurso");
			}

			if (perfis.contains(Perfil.ROLE_VENDEDOR) && !autor.getPerfis().contains(Perfil.ROLE_GERENTE)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar este recurso");
			}
		}
		return null;
	}
}
