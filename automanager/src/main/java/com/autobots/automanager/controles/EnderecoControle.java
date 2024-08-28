package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private EnderecoRepositorio enderecoRepositorio;

	@GetMapping("get/unique/{id}")
	public Endereco obterEndereco(@PathVariable long id) {
		return enderecoRepositorio.getById(id);
	}

	@GetMapping("get/all")
	public List<Endereco> obterEnderecos() {
		List<Endereco> enderecos = enderecoRepositorio.findAll();
		return enderecos;
	}

	@PostMapping("/cadastro")
	public void cadastrarCliente(@RequestBody Endereco endereco, @RequestBody Long clienteId) {
		Cliente cliente = clienteRepositorio.getById(clienteId);
		cliente.setEndereco((endereco));
		enderecoRepositorio.save(endereco);
		clienteRepositorio.save(cliente);
	}

	@PutMapping("/atualizar")
	public void atualizarEndereco(@RequestBody Endereco endereco) {
		Endereco enderecoDb = enderecoRepositorio.getById(endereco.getId());
		EnderecoAtualizador atualizador = new EnderecoAtualizador();
		atualizador.atualizar(enderecoDb, endereco);
		enderecoRepositorio.save(enderecoDb);
	}

	@DeleteMapping("/excluir")
	public void excluirEndereco(@RequestBody Long id) {
		enderecoRepositorio.deleteById(id);
	}
}
