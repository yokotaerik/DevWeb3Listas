package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkVenda;
import com.autobots.automanager.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/venda")
public class VendaControle {

	@Autowired
	private UsuarioRepostorio usuarioRepositorio;
	@Autowired
	private VendaRepositorio vendaRepositorio;
	@Autowired
	private VeiculoRepositorio veiculoRepositorio;
	@Autowired
	private MercadoriaRepositorio mercadoriaRepositorio;
	@Autowired
	private ServicoRepositorio servicoRepositorio;
	@Autowired
	private AdicionadorLinkVenda adicionadorLinkVenda;

	@GetMapping("get/unique/{id}")
	public ResponseEntity<?> obterVenda(@PathVariable long id)
	{
		var venda = vendaRepositorio.getById(id);
		if(venda == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrado");
		} else {
			adicionadorLinkVenda.adicionarLink(venda);
			return ResponseEntity.status(HttpStatus.OK).body(venda);
		}
	}

	@GetMapping("get/all")
	public ResponseEntity<List<Venda>> obterVendas() {
		List<Venda> vendas = vendaRepositorio.findAll();
		if(vendas.isEmpty()){
			return ResponseEntity.notFound().build();
		} else {
			adicionadorLinkVenda.adicionarLink(vendas);
			return ResponseEntity.ok(vendas);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarVenda(@RequestBody Venda venda,
											@RequestParam Long clienteId,
											@RequestParam Long funcionarioId,
											@RequestParam Long veiculoId,
											@RequestParam List<Long> mercadoriasId,
											@RequestParam List<Long> servicosId) {
		var cliente = usuarioRepositorio.findById(clienteId)
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
		var funcionario = usuarioRepositorio.findById(funcionarioId)
				.orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
		var veiculo = veiculoRepositorio.findById(veiculoId)
				.orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

		for (Long id : mercadoriasId) {
			var mercadoria = mercadoriaRepositorio.findById(id)
					.orElseThrow(() -> new RuntimeException("Mercadoria não encontrada"));
			venda.getMercadorias().add(mercadoria);
		}

		for (Long id : servicosId) {
			var servico = servicoRepositorio.findById(id)
					.orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
			venda.getServicos().add(servico);
		}

		venda.setCliente(cliente);
		venda.setFuncionario(funcionario);
		venda.setVeiculo(veiculo);
		vendaRepositorio.save(venda);
		adicionadorLinkVenda.adicionarLink(venda);
		return ResponseEntity.status(HttpStatus.CREATED).body(venda);
	}

//	@PutMapping("/atualizar")
//	public ResponseEntity<?> atualizarVenda(@RequestBody Venda venda) {
//		Venda vendaDb = vendaRepositorio.getById(venda.getId());
//		if(venda == null){
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrado");
//		}
//		atualizarVenda.atualizar(vendaDb, venda);
//		vendaRepositorio.save(vendaDb);
//		adicionadorLinkVenda.adicionarLink(vendaDb);
//		return ResponseEntity.status(HttpStatus.OK).body(vendaDb);
//	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirVenda(@RequestBody Long id) {
		try {
			vendaRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Venda excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir venda");
		}
	}
}
