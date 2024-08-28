package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.TelefoneRepostorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private TelefoneRepostorio telefoneRepositorio;

	@GetMapping("get/unique/{id}")
	public Telefone obterTelefone(@PathVariable long id) {
		return telefoneRepositorio.getById(id);
	}

	@GetMapping("get/all")
	public List<Telefone> obterTelefones() {
		List<Telefone> telefones = telefoneRepositorio.findAll();
		return telefones;
	}

	@PostMapping("/cadastro")
	public void cadastrarCliente(@RequestBody Telefone telefone, @RequestBody Long clienteId) {
		Cliente cliente = clienteRepositorio.getById(clienteId);
		cliente.getTelefones().add(telefone);
		telefoneRepositorio.save(telefone);
		clienteRepositorio.save(cliente);
	}

	@PutMapping("/atualizar")
	public void atualizarTelefone(@RequestBody Telefone telefone) {
		Telefone telefoneDb = telefoneRepositorio.getById(telefone.getId());
		TelefoneAtualizador atualizador = new TelefoneAtualizador();
		atualizador.atualizar(telefoneDb, telefone);
		telefoneRepositorio.save(telefoneDb);
	}

	@DeleteMapping("/excluir")
	public void excluirTelefone(@RequestBody Long id) {
		telefoneRepositorio.deleteById(id);
	}
}
