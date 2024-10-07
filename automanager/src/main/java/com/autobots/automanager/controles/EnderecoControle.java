package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.enderecos.CadastroEnderecoDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.atualizador.EnderecoAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
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
@RequestMapping("/endereco")
public class EnderecoControle {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private EnderecoRepositorio enderecoRepositorio;
	@Autowired
	private AdicionadorLinkEndereco adicionadorLinkEndereco;
	@Autowired
	private EmpresaRepositorio empresaRepositorio;
	@Autowired
	private AuthService authService;

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("get/unique/{id}")
	public ResponseEntity<?> obterEndereco(@PathVariable long id) {
		var endereco = enderecoRepositorio.findById(id).orElse(null);

		if(endereco != null){
			var permissao = verificarPermissao(authService.obterUsuarioLogado(), endereco);
			if (permissao != null) return permissao;

			var adicionadorLinkEndereco = new AdicionadorLinkEndereco();
			adicionadorLinkEndereco.adicionarLink(endereco);
			return ResponseEntity.status(HttpStatus.OK).body(endereco);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("get/all")
	public ResponseEntity<?> obterEnderecos() {
		List<Endereco> enderecos = enderecoRepositorio.findAll();
		if(enderecos.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum endereço encontrado");
		}
		else {
			var adicionadorLinkEndereco = new AdicionadorLinkEndereco();
			adicionadorLinkEndereco.adicionarLink(enderecos);
			return ResponseEntity.status(HttpStatus.OK).body(enderecos);

		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PostMapping("/cadastro/cliente")
	public ResponseEntity<?> cadastrarEnderecoCliente(@RequestBody CadastroEnderecoDTO data) {
		Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(data.getClienteId());

		var endereco =  new Endereco();
		endereco.setEstado(data.getEstado());
		endereco.setCidade(data.getCidade());
		endereco.setBairro(data.getBairro());
		endereco.setRua(data.getRua());
		endereco.setNumero(data.getNumero());
		endereco.setCodigoPostal(data.getCodigoPostal());
		endereco.setInformacoesAdicionais(data.getInformacoesAdicionais());

		if (usuarioOptional.isPresent()) {

			endereco.setUsuario(usuarioOptional.get());
			var permissao = verificarPermissao(authService.obterUsuarioLogado(), endereco);
			if (permissao != null) return permissao;

			Usuario usuario = usuarioOptional.get();
			usuario.setEndereco(endereco);
			enderecoRepositorio.save(endereco);
			usuarioRepositorio.save(usuario);
			adicionadorLinkEndereco.adicionarLink(endereco);
			return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@PostMapping("/cadastro/empresa")
	public ResponseEntity<?> cadastrarEnderecoEmpresa(@RequestBody CadastroEnderecoDTO data) {
		Optional<Empresa> empresaOptional = empresaRepositorio.findById(data.getEmpresaId());

		var endereco =  new Endereco();
		endereco.setEstado(data.getEstado());
		endereco.setCidade(data.getCidade());
		endereco.setBairro(data.getBairro());
		endereco.setRua(data.getRua());
		endereco.setNumero(data.getNumero());
		endereco.setCodigoPostal(data.getCodigoPostal());
		endereco.setInformacoesAdicionais(data.getInformacoesAdicionais());

		if (empresaOptional.isPresent()) {
			Empresa empresa = empresaOptional.get();
			empresa.setEndereco(endereco);
			enderecoRepositorio.save(endereco);
			empresaRepositorio.save(empresa);
			adicionadorLinkEndereco.adicionarLink(endereco);
			return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco endereco) {
		Endereco enderecoDb = enderecoRepositorio.getById(endereco.getId());

		if(enderecoDb == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");

		var permissao = verificarPermissao(authService.obterUsuarioLogado(), enderecoDb);
		if (permissao != null) return permissao;

		EnderecoAtualizador atualizador = new EnderecoAtualizador();
		atualizador.atualizar(enderecoDb, endereco);

		enderecoRepositorio.save(enderecoDb);
		adicionadorLinkEndereco.adicionarLink(enderecoDb);

		return ResponseEntity.status(HttpStatus.OK).body(enderecoDb);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirEndereco(@PathVariable Long id) {
		try{
			var endereco = enderecoRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Endereço não encontrado"));

			var permissao = verificarPermissao(authService.obterUsuarioLogado(), endereco);
			if (permissao != null) return permissao;

			enderecoRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Endereço excluído com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");
		}
	}

	public ResponseEntity<?> verificarPermissao(Usuario autor, Endereco endereco) {
		if(endereco.getEmpresa() != null && !autor.getPerfis().contains(Perfil.ROLE_ADMIN)){
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar este recurso");
		}

		if(endereco.getUsuario() != null){
			var perfis = endereco.getUsuario().getPerfis();

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
