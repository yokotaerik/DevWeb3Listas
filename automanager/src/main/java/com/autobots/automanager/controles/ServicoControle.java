package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.servico.CadastroServicoDTO;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkServico;
import com.autobots.automanager.modelos.atualizador.ServicoAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

	@Autowired
	private ServicoRepositorio servicoRepositorio;

	@Autowired
	private EmpresaRepositorio empresaRepositorio;

	@Autowired
	private AdicionadorLinkServico adicionadorLinkServico;

	@Autowired
	private ServicoAtualizador atualizador;

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("get/unique/{id}")
	public ResponseEntity<?> obterServico(@PathVariable long id)
	{
		var servico = servicoRepositorio.getById(id);
		if(servico == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Servico não encontrado");
		} else {
			adicionadorLinkServico.adicionarLink(servico);
			return ResponseEntity.status(HttpStatus.OK).body(servico);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("get/all")
	public ResponseEntity<List<Servico>> obterServicos() {
		List<Servico> servicos = servicoRepositorio.findAll();
		if(servicos.isEmpty()){
			return ResponseEntity.notFound().build();
		} else {
			adicionadorLinkServico.adicionarLink(servicos);
			return ResponseEntity.ok(servicos);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarServico(@RequestBody CadastroServicoDTO data) throws Exception {
		var empresa = empresaRepositorio.findById(data.getEmpresaId()).orElseThrow(() -> new Exception("Empresa não encontrada"));

		var servico = new Servico();
		servico.setNome(data.getNome());
		servico.setValor(data.getValor());
		servico.setDescricao(data.getDescricao());

		empresa.getServicos().add(servico);

		servicoRepositorio.save(servico);
		empresaRepositorio.save(empresa);

		return ResponseEntity.status(HttpStatus.CREATED).body(servico);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarServico(@RequestBody Servico servico) {
		Servico servicoDb = servicoRepositorio.getById(servico.getId());
		if(servico == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Servico não encontrado");
		}
		atualizador.atualizar(servicoDb, servico);
		servicoRepositorio.save(servicoDb);
		adicionadorLinkServico.adicionarLink(servicoDb);
		return ResponseEntity.status(HttpStatus.OK).body(servicoDb);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirServico(@PathVariable Long id) {
		try {
			servicoRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Servico excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir servico");
		}
	}
}
