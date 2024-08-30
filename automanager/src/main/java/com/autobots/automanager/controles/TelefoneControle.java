package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepostorio;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private TelefoneRepostorio telefoneRepositorio;
	@Autowired
	private TelefoneAtualizador atualizador;
	@Autowired
	private AdicionadorLinkTelefone adicionadorLinkTelefone;

	@GetMapping("get/unique/{id}")
	public ResponseEntity<?> obterTelefone(@PathVariable long id) {
		var telefone = telefoneRepositorio.getById(id);

		if(telefone == null){ return ResponseEntity.status(404).body("Telefone não encontrado"); }

		adicionadorLinkTelefone.adicionarLink(telefone);

		return ResponseEntity.status(200).body(telefone);
	}

	@GetMapping("get/all")
	public ResponseEntity<List<Telefone>> obterTelefones() {
		List<Telefone> telefones = telefoneRepositorio.findAll();
		adicionadorLinkTelefone.adicionarLink(telefones);
		return ResponseEntity.status(200).body(telefones);
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarCliente(@RequestBody Telefone telefone, @RequestBody Long clienteId) {
		Cliente cliente = clienteRepositorio.getById(clienteId);
		if (cliente == null) {
			return ResponseEntity.status(404).body("Cliente não encontrado");
		}
		cliente.getTelefones().add(telefone);
		telefoneRepositorio.save(telefone);
		clienteRepositorio.save(cliente);
		return ResponseEntity.status(201).body(telefone);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarTelefone(@RequestBody Telefone telefone) {
		Telefone telefoneDb = telefoneRepositorio.getById(telefone.getId());

		if(telefoneDb.getId() != telefone.getId()){
			return ResponseEntity.status(404).body("Telefone não encontrado");
		}

		atualizador.atualizar(telefoneDb, telefone);

		telefoneRepositorio.save(telefoneDb);

		return ResponseEntity.status(200).body(telefoneDb);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirTelefone(@RequestBody Long id) {
		try {
			telefoneRepositorio.deleteById(id);
			return ResponseEntity.status(200).body("Telefone excluído com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(400).body("Telefone não excluido");
		}
	}
}
