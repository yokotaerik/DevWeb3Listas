package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.empresa.EmpresaCompletaDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelos.atualizador.EmpresaAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {

	@Autowired
	private EmpresaRepositorio empresaRepositorio;

	@Autowired
	private AdicionadorLinkEmpresa adicionadorLinkEmpresa;

	@Autowired
	private EmpresaAtualizador atualizador;

	@GetMapping("get/unique/{id}")
	public ResponseEntity<?> obterEmpresa(@PathVariable long id)
	{
		var empresa = empresaRepositorio.getById(id);
		if(empresa == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrado");
		} else {
			var dto = EmpresaCompletaDTO.from(empresa);
			adicionadorLinkEmpresa.adicionarLink(dto);
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
	}

	@GetMapping("get/all")
	public ResponseEntity<?> obterEmpresas() {

		List<Empresa> empresas = empresaRepositorio.findAll();
		if(empresas.isEmpty()){
			return ResponseEntity.notFound().build();
		} else {
			var dtos = EmpresaCompletaDTO.from(empresas);
			adicionadorLinkEmpresa.adicionarLink(dtos);
			return ResponseEntity.ok(dtos);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarEmpresa(@RequestBody Empresa empresa) {
		empresa.setCadastro(new Date());
		empresaRepositorio.save(empresa);

		var dto = EmpresaCompletaDTO.from(empresa);
		adicionadorLinkEmpresa.adicionarLink(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(dto);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEmpresa(@RequestBody Empresa empresa) {
		Empresa empresaDb = empresaRepositorio.getById(empresa.getId());
		if(empresa == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrado");
		}
		atualizador.atualizar(empresaDb, empresa);
		empresaRepositorio.save(empresaDb);
		var dto = EmpresaCompletaDTO.from(empresaDb);
		adicionadorLinkEmpresa.adicionarLink(dto);
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirEmpresa(@PathVariable Long id) {
		try {
			empresaRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Empresa excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir empresa");
		}
	}
}
