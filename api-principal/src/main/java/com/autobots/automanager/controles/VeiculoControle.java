package com.autobots.automanager.controles;

import com.autobots.automanager.dtos.veiculo.CadastroVeiculoDTO;
import com.autobots.automanager.dtos.veiculo.VeiculoComDonoDTO;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelos.adicionadorLinks.AdicionadorLinkVeiculo;
import com.autobots.automanager.modelos.atualizador.VeiculoAtualizador;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

	@Autowired
	private VeiculoRepositorio veiculoRepositorio;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private AdicionadorLinkVeiculo adicionadorLinkVeiculo;

	@Autowired
	private VeiculoAtualizador atualizador;

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("/{id}")
	public ResponseEntity<?> obterVeiculo(@PathVariable long id)
	{
		var veiculo = veiculoRepositorio.getById(id);
		if(veiculo == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo não encontrado");
		} else {
			var dto = VeiculoComDonoDTO.from(veiculo);
			adicionadorLinkVeiculo.adicionarLink(dto);
			return ResponseEntity.status(HttpStatus.OK).body(dto);
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@GetMapping("")
	public ResponseEntity<?> obterVeiculos() {
		List<Veiculo> veiculos = veiculoRepositorio.findAll();

		var dtos = VeiculoComDonoDTO.from(veiculos);
		adicionadorLinkVeiculo.adicionarLink(dtos);
		return ResponseEntity.ok(dtos);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PostMapping("/cadastro")
	public ResponseEntity<?> cadastrarVeiculo(@RequestBody CadastroVeiculoDTO data) throws Exception {
		var usuario = usuarioRepositorio.findById(data.getIdProprietario());

		if(usuario.isPresent()){
			var veiculo = new Veiculo();
			veiculo.setModelo(data.getModelo());
			veiculo.setPlaca(data.getPlaca());
			veiculo.setTipo(data.getTipo());

			var usuarioDb = usuario.get();
			usuarioDb.getVeiculos().add(veiculo);
			veiculo.setProprietario(usuarioDb);
			veiculoRepositorio.save(veiculo);
			usuarioRepositorio.save(usuarioDb);

			var dto = VeiculoComDonoDTO.from(veiculo);
			adicionadorLinkVeiculo.adicionarLink(dto);
			return ResponseEntity.status(HttpStatus.CREATED).body(dto);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrada");
		}
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@PutMapping("/atualizar")
	public ResponseEntity<?> atualizarVeiculo(@RequestBody Veiculo veiculo) {
		Veiculo veiculoDb = veiculoRepositorio.getById(veiculo.getId());
		if(veiculo == null){
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Veiculo não encontrado");
		}
		atualizador.atualizar(veiculoDb, veiculo);
		veiculoRepositorio.save(veiculoDb);
		var dto = VeiculoComDonoDTO.from(veiculoDb);
		adicionadorLinkVeiculo.adicionarLink(dto);
		return ResponseEntity.status(HttpStatus.OK).body(dto);
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'GERENTE', 'VENDEDOR')")
	@DeleteMapping("/excluir/{id}")
	public ResponseEntity<?> excluirVeiculo(@PathVariable Long id) {
		try {
			var veiculo = veiculoRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Veiculo não encontrado"));

			veiculo.getProprietario().getVeiculos().remove(veiculo);

			usuarioRepositorio.save(veiculo.getProprietario());
			veiculoRepositorio.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Veiculo excluído com sucesso");
		} catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao excluir veiculo");
		}
	}
}
