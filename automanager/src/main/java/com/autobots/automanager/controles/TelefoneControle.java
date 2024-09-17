package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.telefone.CadastroTelefoneDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.atualizador.TelefoneAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepostorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private TelefoneRepostorio telefoneRepositorio;
	@Autowired
	private AdicionadorLinkTelefone adicionadorLinkTelefone;
	@Autowired
	private EmpresaRepositorio empresaRepositorio;

	@GetMapping("get/unique/{id}")
	public ResponseEntity<?> obterTelefone(@PathVariable long id) {
		var telefone = telefoneRepositorio.getById(id);
		if(telefone.getId() == id){
			var adicionadorLinkTelefone = new AdicionadorLinkTelefone();
			adicionadorLinkTelefone.adicionarLink(telefone);
			return ResponseEntity.status(HttpStatus.OK).body(telefone);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");
		}
	}

	@GetMapping("get/all")
	public ResponseEntity<?> obterTelefones() {
		List<Telefone> telefones = telefoneRepositorio.findAll();
		if(telefones.isEmpty())
		{
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum endereço encontrado");
		}
		else {
			var adicionadorLinkTelefone = new AdicionadorLinkTelefone();
			adicionadorLinkTelefone.adicionarLink(telefones);
			return ResponseEntity.status(HttpStatus.OK).body(telefones);

		}
	}

	@PostMapping("/cadastro/cliente")
	public ResponseEntity<?> cadastrarTelefoneCliente(@RequestBody CadastroTelefoneDTO data){
		Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(data.getClienteId());

		if (usuarioOptional.isPresent()) {
			var telefone = new Telefone();
			telefone.setDdd(data.getDdd());
			telefone.setNumero(data.getNumero());

			Usuario usuario = usuarioOptional.get();
			usuario.getTelefones().add(telefone);
			telefoneRepositorio.save(telefone);
			usuarioRepositorio.save(usuario);
			adicionadorLinkTelefone.adicionarLink(telefone);
			return ResponseEntity.status(HttpStatus.CREATED).body(telefone);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
		}
	}

	@PostMapping("/cadastro/empresa")
	public ResponseEntity<?> cadastrarTelefoneEmpresa(@RequestBody CadastroTelefoneDTO data) {
		Optional<Empresa> empresaOptional = empresaRepositorio.findById(data.getEmpresaId());

		if (empresaOptional.isPresent()) {
			var telefone = new Telefone();
			telefone.setDdd(data.getDdd());
			telefone.setNumero(data.getNumero());

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

		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefoneDb, telefone);

		telefoneRepositorio.save(telefoneDb);
		adicionadorLinkTelefone.adicionarLink(telefoneDb);

		return ResponseEntity.status(HttpStatus.OK).body(telefoneDb);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirTelefone(@PathVariable Long id) {
		try{
			telefoneRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Endereço excluído com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");
		}
	}
}
