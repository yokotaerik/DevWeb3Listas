package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.venda.CadastroVendaDTO;
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
	private UsuarioRepositorio usuarioRepositorio;
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
	public ResponseEntity<?> obterVendas() {
		List<Venda> vendas = vendaRepositorio.findAll();
		if(vendas.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma venda encontrada");
		} else {
			adicionadorLinkVenda.adicionarLink(vendas);
			return ResponseEntity.ok(vendas);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarVenda(@RequestBody CadastroVendaDTO data) {
		var cliente = usuarioRepositorio.findById(data.getClienteId())
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
		var funcionario = usuarioRepositorio.findById(data.getFuncionarioId())
				.orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));


		var venda = new Venda();
		venda.setCadastro(data.getCadastro());
		venda.setIdentificacao(data.getIdentificacao());

		if(!data.getServicosId().isEmpty()){
			var veiculo = veiculoRepositorio.findById(data.getVeiculoId())
					.orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
			venda.setVeiculo(veiculo);
		}

		for (Long id : data.getMercadoriasId()) {
			var mercadoria = mercadoriaRepositorio.findById(id)
					.orElseThrow(() -> new RuntimeException("Mercadoria não encontrada"));
			mercadoria.setQuantidade(mercadoria.getQuantidade() - 1);
			if(mercadoria.getQuantidade() < 0){
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade de mercadoria insuficiente");
			}
			mercadoriaRepositorio.save(mercadoria);
			venda.getMercadorias().add(mercadoria);
		}

		for (Long id : data.getServicosId()) {
			var servico = servicoRepositorio.findById(id)
					.orElseThrow(() -> new RuntimeException("Serviço não encontrado"));
			venda.getServicos().add(servico);
		}

		venda.setCliente(cliente);
		venda.setFuncionario(funcionario);
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

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirVenda(@PathVariable Long id) {
		try {
			vendaRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Venda excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir venda");
		}
	}
}
