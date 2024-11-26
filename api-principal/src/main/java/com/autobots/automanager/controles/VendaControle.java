package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.venda.CadastroVendaDTO;
import com.autobots.automanager.dtos.venda.VendaCompletaDTO;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkVenda;
import com.autobots.automanager.repositorios.*;
import com.autobots.automanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
	@Autowired
	private AuthService authService;

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR', 'CLIENTE')")
	@GetMapping("get/minhas-vendas")
	public ResponseEntity<?> obterMinhasVendas() {
		var usuarioLogado = authService.obterUsuarioLogado();

		var vendas = vendaRepositorio.findAll();

		var vendasUsuario = vendas.stream()
			.filter(venda -> Objects.equals(venda.getCliente().getId(), usuarioLogado.getId()))
			.collect(Collectors.toList());

		return ResponseEntity.ok(vendasUsuario);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@GetMapping("/{id}")
	public ResponseEntity<?> obterVenda(@PathVariable long id)
	{
		var venda = vendaRepositorio.findById(id);
		if(venda.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrado");
		} else {
			var vendaDTO = VendaCompletaDTO.fromEntity(venda.get());
			adicionadorLinkVenda.adicionarLink(vendaDTO);
			return ResponseEntity.status(HttpStatus.OK).body(vendaDTO);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@GetMapping("")
	public ResponseEntity<?> obterVendas() {
		try {
			List<Venda> vendas = vendaRepositorio.findAll();
			if (vendas.isEmpty()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhuma venda encontrada");
			} else {
				var dtos = VendaCompletaDTO.from(vendas);
				adicionadorLinkVenda.adicionarLink(dtos);
				return ResponseEntity.ok(dtos);
			}
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao buscar vendas: " + ex.getMessage());
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarVenda(@RequestBody CadastroVendaDTO data) {
		var cliente = usuarioRepositorio.findById(data.getClienteId())
				.orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
		var funcionario = usuarioRepositorio.findById(data.getFuncionarioId())
				.orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));

		var usuarioLogado = authService.obterUsuarioLogado();
		if(!usuarioLogado.getPerfis().contains(Perfil.ROLE_GERENTE)
		|| !usuarioLogado.getPerfis().contains(Perfil.ROLE_ADMIN)){
			if(!funcionario.getId().equals(usuarioLogado.getId())){
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autorizado");
			}
		}

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
		var vendaDTO = VendaCompletaDTO.fromEntity(venda);
		adicionadorLinkVenda.adicionarLink(vendaDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(vendaDTO);
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
