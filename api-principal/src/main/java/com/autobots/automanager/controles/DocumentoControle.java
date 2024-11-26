package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.documento.CadastroDocumentoDTO;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.atualizador.DocumentoAtualizador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
	@Autowired
	private AuthService authService;

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("/{id}")
	public ResponseEntity<?> obterDocumento(@PathVariable long id)
	{
		var documento = documentoRepositorio.getById(id);
		if(documento == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado");
		} else {
			var permisao = verificarPermissao(authService.obterUsuarioLogado(), documento);
			if(permisao != null) return permisao;

			adicionadorLinkDocumento.adicionarLink(documento);
			return ResponseEntity.status(HttpStatus.OK).body(documento);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping("")
	public ResponseEntity<List<Documento>> obterDocumentos() {
		List<Documento> documentos = documentoRepositorio.findAll();
		if(documentos.isEmpty()){
			return ResponseEntity.notFound().build();
		} else {
			adicionadorLinkDocumento.adicionarLink(documentos);
			return ResponseEntity.ok(documentos);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarDocumento(@RequestBody CadastroDocumentoDTO data) {
		Usuario cliente = usuarioRepositorio.findById(data.getClienteId()).orElseThrow(
				() -> new RuntimeException("Cliente não encontrado"));

		var documento = new Documento();
		documento.setTipo(data.getTipo());
		documento.setDataEmissao(data.getDataEmissao());
		documento.setNumero(data.getNumero());
		documento.setUsuario(cliente);

		var permisao = verificarPermissao(authService.obterUsuarioLogado(), documento);
		if(permisao != null) return permisao;

		cliente.getDocumentos().add(documento);
		documentoRepositorio.save(documento);
		usuarioRepositorio.save(cliente);
		adicionadorLinkDocumento.adicionarLink(documento);
		return ResponseEntity.status(HttpStatus.CREATED).body(documento);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarDocumento(@RequestBody Documento documento) {
		Documento documentoDb = documentoRepositorio.getById(documento.getId());
		if(documento == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado");
		}

		var permisao = verificarPermissao(authService.obterUsuarioLogado(), documento);
		if(permisao != null) return permisao;

		atualizarDocumento.atualizar(documentoDb, documento);
		documentoRepositorio.save(documentoDb);
		adicionadorLinkDocumento.adicionarLink(documentoDb);
		return ResponseEntity.status(HttpStatus.OK).body(documentoDb);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirDocumento(@PathVariable Long id) {
		try {
			var documento = documentoRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Documento não encontrado"));

			documento.getUsuario().getDocumentos().remove(documento);

			var permisao = verificarPermissao(authService.obterUsuarioLogado(), documento);
			if(permisao != null) return permisao;

			documentoRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Documento excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir documento");
		}
	}

	public ResponseEntity<?> verificarPermissao(Usuario autor, Documento documento) {
		if(documento.getUsuario() != null){
			var perfis = documento.getUsuario().getPerfis();

			if (perfis.contains(Perfil.ROLE_ADMIN) && !autor.getPerfis().contains(Perfil.ROLE_ADMIN)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar este recurso");
			}

			if (perfis.contains(Perfil.ROLE_GERENTE) && !autor.getPerfis().contains(Perfil.ROLE_GERENTE)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar este recurso");
			}

			if (perfis.contains(Perfil.ROLE_VENDEDOR) && !autor.getPerfis().contains(Perfil.ROLE_GERENTE)) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Você não tem permissão para acessar este recurso");
			}
		}
		return null;
	}
}
