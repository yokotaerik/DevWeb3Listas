package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.ClienteAtualizador;
import com.autobots.automanager.modelo.ClienteSelecionador;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

	@Autowired
	private ClienteRepositorio clienteRepositorio;
	@Autowired
	private DocumentoRepositorio documentoRepositorio;

	@GetMapping("get/unique/{id}")
	public Documento obterDocumento(@PathVariable long id) {
		return documentoRepositorio.getById(id);
	}

	@GetMapping("get/all")
	public List<Documento> obterDocumentos() {
		List<Documento> documentos = documentoRepositorio.findAll();
		return documentos;
	}

	@PostMapping("/cadastro")
	public void cadastrarCliente(@RequestBody Documento documento, @RequestBody Long clienteId) {
		Cliente cliente = clienteRepositorio.getById(clienteId);
		cliente.getDocumentos().add(documento);
		documentoRepositorio.save(documento);
		clienteRepositorio.save(cliente);
	}

	@PutMapping("/atualizar")
	public void atualizarDocumento(@RequestBody Documento documento) {
		Documento documentoDb = documentoRepositorio.getById(documento.getId());
		DocumentoAtualizador atualizador = new DocumentoAtualizador();
		atualizador.atualizar(documentoDb, documento);
		documentoRepositorio.save(documentoDb);
	}

	@DeleteMapping("/excluir")
	public void excluirDocumento(@RequestBody Long id) {
		documentoRepositorio.deleteById(id);
	}
}
