package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.documento.CadastroDocumentoDTO;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.atualizador.DocumentoAtualizador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	@Autowired
	private DocumentoRepositorio documentoRepositorio;
	@Autowired
	private AdicionadorLinkDocumento adicionadorLinkDocumento;
	@Autowired
	private DocumentoAtualizador atualizarDocumento;

	@GetMapping("get/unique/{id}")
	public ResponseEntity<?> obterDocumento(@PathVariable long id)
	{
		var documento = documentoRepositorio.getById(id);
		if(documento == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado");
		} else {
			adicionadorLinkDocumento.adicionarLink(documento);
			return ResponseEntity.status(HttpStatus.OK).body(documento);
		}
	}

	@GetMapping("get/all")
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> documentos = documentoRepositorio.findAll();
		if(documentos.isEmpty()){
			return ResponseEntity.notFound().build();
		} else {
			adicionadorLinkDocumento.adicionarLink(documentos);
			return ResponseEntity.ok(documentos);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarDocumento(@RequestBody CadastroDocumentoDTO data) {
		Usuario cliente = usuarioRepositorio.findById(data.getClienteId()).orElseThrow(
				() -> new RuntimeException("Cliente não encontrado"));

		var documento = new Documento();
		documento.setTipo(data.getTipo());
		documento.setDataEmissao(data.getDataEmissao());
		documento.setNumero(data.getNumero());

		cliente.getDocumentos().add(documento);
		documentoRepositorio.save(documento);
		usuarioRepositorio.save(cliente);
		adicionadorLinkDocumento.adicionarLink(documento);
		return ResponseEntity.status(HttpStatus.CREATED).body(documento);
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarDocumento(@RequestBody Documento documento) {
		Documento documentoDb = documentoRepositorio.getById(documento.getId());
		if(documento == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado");
		}
		atualizarDocumento.atualizar(documentoDb, documento);
		documentoRepositorio.save(documentoDb);
		adicionadorLinkDocumento.adicionarLink(documentoDb);
		return ResponseEntity.status(HttpStatus.OK).body(documentoDb);
	}

	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirDocumento(@PathVariable Long id) {
		try {
			documentoRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Documento excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir documento");
		}
	}
}
