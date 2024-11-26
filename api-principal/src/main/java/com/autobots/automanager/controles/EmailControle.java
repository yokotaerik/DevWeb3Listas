package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.emails.CadastroEmailDTO;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkEmail;
import com.autobots.automanager.modelos.atualizador.EmailAtualizador;
import com.autobots.automanager.repositorios.EmailRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/email")
public class EmailControle {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private EmailRepositorio emailRepositorio;
	@Autowired
	private AdicionadorLinkEmail adicionadorLinkEmail;
	@Autowired
	private EmailAtualizador atualizarEmail;
	@Autowired
	private AuthService authService;

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("/{id}")
	public ResponseEntity<?> obterEmail(@PathVariable long id)
	{
		var email = emailRepositorio.getById(id);
		if(email == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email não encontrado");
		} else {

			var verificacao = verificarPermissao(authService.obterUsuarioLogado(), email);
			if(verificacao != null) return verificacao;

			adicionadorLinkEmail.adicionarLink(email);
			return ResponseEntity.status(HttpStatus.OK).body(email);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("")
	public ResponseEntity<List<Email>> obterEmails() {
		List<Email> emails = emailRepositorio.findAll();
		if(emails.isEmpty()){
			return ResponseEntity.notFound().build();
		} else {
			adicionadorLinkEmail.adicionarLink(emails);
			return ResponseEntity.ok(emails);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarEmail(@RequestBody CadastroEmailDTO data) {
		var cliente = usuarioRepositorio.findById(data.getClienteId()).orElseThrow(
				() -> new RuntimeException("Cliente não encontrado"));

		var email = new Email(data.getEmail());

		var validacao = verificarPermissao(authService.obterUsuarioLogado(), email);
		if(validacao != null) return validacao;

		// Adiciona o email ao cliente
		email.setUsuario(cliente);
		cliente.getEmails().add(email);

		emailRepositorio.save(email);
		usuarioRepositorio.save(cliente);
		adicionadorLinkEmail.adicionarLink(email);

		return ResponseEntity.status(HttpStatus.CREATED).body(email);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEmail(@RequestBody Email email) {
		var emailDb = emailRepositorio.findById(email.getId()).orElseThrow(
				() -> new RuntimeException("Email não encontrado"));

		var validacao = verificarPermissao(authService.obterUsuarioLogado(), emailDb);
		if(validacao != null) return validacao;

		atualizarEmail.atualizar(emailDb, email);
		emailRepositorio.save(emailDb);
		adicionadorLinkEmail.adicionarLink(emailDb);
		return ResponseEntity.status(HttpStatus.OK).body(emailDb);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirEmail(@PathVariable Long id) {
		try {
			var email = emailRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Email não encontrado"));

			email.getUsuario().getEmails().remove(email);

			var validacao = verificarPermissao(authService.obterUsuarioLogado(), email);
			if(validacao != null) return validacao;

			emailRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Email excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir email");
		}
	}

	public ResponseEntity<?> verificarPermissao(Usuario autor, Email email) {
		if(email.getUsuario() != null){
			var perfis = email.getUsuario().getPerfis();

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
