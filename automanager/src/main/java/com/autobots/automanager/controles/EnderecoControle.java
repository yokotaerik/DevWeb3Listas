package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.enderecos.CadastroEnderecoDTO;
import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.EnderecoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.EnderecoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enderecos")
public class 	EnderecoControle {

	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private EnderecoRepositorio enderecoRepositorio;
	@Autowired
	private AdicionadorLinkEndereco adicionadorLinkEndereco;

	@GetMapping("/{id}")
	public ResponseEntity<?> obterEndereco(@PathVariable long id) {
		var endereco = enderecoRepositorio.getById(id);
		if(endereco.getId() == id){
			var adicionadorLinkEndereco = new AdicionadorLinkEndereco();
			adicionadorLinkEndereco.adicionarLink(endereco);
			return ResponseEntity.status(HttpStatus.OK).body(endereco);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");
		}
	}

	@GetMapping("")
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

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarCliente(@RequestBody CadastroEnderecoDTO data) {
		Cliente cliente = clienteRepositorio.getById(data.getClienteId());
		if(cliente == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
		else {
			Endereco endereco = new Endereco();
			endereco.setEstado(data.getEstado());
			endereco.setCidade(data.getCidade());
			endereco.setBairro(data.getBairro());
			endereco.setRua(data.getRua());
			endereco.setNumero(data.getNumero());
			endereco.setCodigoPostal(data.getCodigoPostal());
			endereco.setInformacoesAdicionais(data.getInformacoesAdicionais());

			cliente.setEndereco((endereco));
			enderecoRepositorio.save(endereco);
			clienteRepositorio.save(cliente);
			adicionadorLinkEndereco.adicionarLink(endereco);
			return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEndereco(@RequestBody Endereco endereco) {
		Endereco enderecoDb = enderecoRepositorio.getById(endereco.getId());

		if(enderecoDb == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");

		EnderecoAtualizador atualizador = new EnderecoAtualizador();
		atualizador.atualizar(enderecoDb, endereco);

		enderecoRepositorio.save(enderecoDb);
		adicionadorLinkEndereco.adicionarLink(enderecoDb);

		return ResponseEntity.status(HttpStatus.OK).body(enderecoDb);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirEndereco(@RequestBody Long id) {
		try{
			enderecoRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Endereço excluído com sucesso");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado");
		}
	}
}
