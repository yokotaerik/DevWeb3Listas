package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.mercadoria.CadastroMercadoriaDTO;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkMercadoria;
import com.autobots.automanager.modelos.atualizador.MercadoriaAtualizador;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/mercadorias")
public class MercadoriaControle {

	@Autowired
	private MercadoriaRepositorio mercadoriaRepositorio;

	@Autowired
	private EmpresaRepositorio empresaRepositorio;

	@Autowired
	private AdicionadorLinkMercadoria adicionadorLinkMercadoria;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private MercadoriaAtualizador atualizador;

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("empresaRepositorio/{id}")
	public ResponseEntity<?> obterMercadoria(@PathVariable long id)
	{
		var mercadoria = mercadoriaRepositorio.getById(id);
		if(mercadoria == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mercadoria não encontrado");
		} else {
			adicionadorLinkMercadoria.adicionarLink(mercadoria);
			return ResponseEntity.status(HttpStatus.OK).body(mercadoria);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("get/all")
	public ResponseEntity<List<Mercadoria>> obterMercadorias() {
		List<Mercadoria> mercadorias = mercadoriaRepositorio.findAll();
		if(mercadorias.isEmpty()){
			return ResponseEntity.notFound().build();
		} else {
			adicionadorLinkMercadoria.adicionarLink(mercadorias);
			return ResponseEntity.ok(mercadorias);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarMercadoria(@RequestBody CadastroMercadoriaDTO data) throws Exception {
		var empresa = empresaRepositorio.findById(data.getEmpresaId());
		var fornecedor = usuarioRepositorio.findById(data.getFornecedorId());

		if(fornecedor.isEmpty()){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Fornecedor não encontrado");
		}

		var fornecedorDb = fornecedor.get();

		if(empresa.isPresent()){
			var mercadoria = new Mercadoria();
			mercadoria.setNome(data.getNome());
			mercadoria.setQuantidade(data.getQuantidade());
			mercadoria.setValor(data.getValor());
			mercadoria.setDescricao(data.getDescricao());
			mercadoria.setCadastro(new Date());
			mercadoria.setFabricao(data.getFabricao());
			mercadoria.setValidade(data.getValidade());

			var empresaDb = empresa.get();
			empresaDb.getMercadorias().add(mercadoria);
			mercadoriaRepositorio.save(mercadoria);
			empresaRepositorio.save(empresaDb);

			fornecedorDb.getMercadorias().add(mercadoria);
			usuarioRepositorio.save(fornecedorDb);

			adicionadorLinkMercadoria.adicionarLink(mercadoria);

			return ResponseEntity.status(HttpStatus.CREATED).body(mercadoria);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarMercadoria(@RequestBody Mercadoria mercadoria) {
		Mercadoria mercadoriaDb = mercadoriaRepositorio.getById(mercadoria.getId());
		if(mercadoria == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mercadoria não encontrado");
		}
		atualizador.atualizar(mercadoriaDb, mercadoria);
		mercadoriaRepositorio.save(mercadoriaDb);
		adicionadorLinkMercadoria.adicionarLink(mercadoriaDb);
		return ResponseEntity.status(HttpStatus.OK).body(mercadoriaDb);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirMercadoria(@PathVariable Long id) {
		try {
			mercadoriaRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Mercadoria excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir mercadoria");
		}
	}
}
