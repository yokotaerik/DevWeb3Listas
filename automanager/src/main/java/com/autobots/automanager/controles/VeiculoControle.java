package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkVeiculo;
import com.autobots.automanager.modelos.atualizador.VeiculoAtualizador;
import com.autobots.automanager.repositorios.UsuarioRepostorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

	@Autowired
	private VeiculoRepositorio veiculoRepositorio;

	@Autowired
	private UsuarioRepostorio usuarioRepositorio;

	@Autowired
	private AdicionadorLinkVeiculo adicionadorLinkVeiculo;

	@Autowired
	private VeiculoAtualizador atualizador;

	@GetMapping("get/unique/{id}")
	public ResponseEntity<?> obterVeiculo(@PathVariable long id)
	{
		var veiculo = veiculoRepositorio.getById(id);
		if(veiculo == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo não encontrado");
		} else {
			adicionadorLinkVeiculo.adicionarLink(veiculo);
			return ResponseEntity.status(HttpStatus.OK).body(veiculo);
		}
	}

	@GetMapping("get/all")
	public ResponseEntity<List<Veiculo>> obterVeiculos() {
		List<Veiculo> veiculos = veiculoRepositorio.findAll();
		if(veiculos.isEmpty()){
			return ResponseEntity.notFound().build();
		} else {
			adicionadorLinkVeiculo.adicionarLink(veiculos);
			return ResponseEntity.ok(veiculos);
		}
	}

	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarVeiculo(@RequestBody Veiculo veiculo, @RequestParam Long usuarioId) throws Exception {
		var usuario = usuarioRepositorio.findById(usuarioId);

		if(usuario.isPresent()){
			var usuarioDb = usuario.get();
			usuarioDb.getVeiculos().add(veiculo);
			veiculoRepositorio.save(veiculo);
			usuarioRepositorio.save(usuarioDb);

			return ResponseEntity.status(HttpStatus.CREATED).body(veiculo);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrada");
		}
	}

	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarVeiculo(@RequestBody Veiculo veiculo) {
		Veiculo veiculoDb = veiculoRepositorio.getById(veiculo.getId());
		if(veiculo == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo não encontrado");
		}
		atualizador.atualizar(veiculoDb, veiculo);
		veiculoRepositorio.save(veiculoDb);
		adicionadorLinkVeiculo.adicionarLink(veiculoDb);
		return ResponseEntity.status(HttpStatus.OK).body(veiculoDb);
	}

	@DeleteMapping("/excluir")
	public ResponseEntity<?> excluirVeiculo(@RequestBody Long id) {
		try {
			veiculoRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Veiculo excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir veiculo");
		}
	}
}
