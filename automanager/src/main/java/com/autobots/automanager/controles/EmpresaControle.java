package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelos.atualizador.EmpresaAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
			adicionadorLinkEmpresa.adicionarLink(empresa);
			return ResponseEntity.status(HttpStatus.OK).body(empresa);
		}
	}

	@GetMapping("get/all")
	public ResponseEntity<List<Empresa>> obterEmpresas() {
		List<Empresa> empresas = empresaRepositorio.findAll();
		if(empresas.isEmpty()){
			return ResponseEntity.notFound().build();
		} else {
			adicionadorLinkEmpresa.adicionarLink(empresas);
			return ResponseEntity.ok(empresas);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarEmpresa(@RequestBody Empresa empresa) {
		empresaRepositorio.save(empresa);

		adicionadorLinkEmpresa.adicionarLink(empresa);

		return ResponseEntity.status(HttpStatus.CREATED).body(empresa);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarEmpresa(@RequestBody Empresa empresa) {
		Empresa empresaDb = empresaRepositorio.getById(empresa.getId());
		if(empresa == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrado");
		}
		atualizador.atualizar(empresaDb, empresa);
		empresaRepositorio.save(empresaDb);
		adicionadorLinkEmpresa.adicionarLink(empresaDb);
		return ResponseEntity.status(HttpStatus.OK).body(empresaDb);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirEmpresa(@RequestBody Long id) {
		try {
			empresaRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Empresa excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir empresa");
		}
	}
}
