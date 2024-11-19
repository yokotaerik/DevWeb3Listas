package com.comunicacao.sistema.controles;

import java.util.List;

import com.comunicacao.sistema.dtos.responseDTO.UsuarioResponseDTO;
import com.comunicacao.sistema.dtos.usuario.UsuarioDTO;
import com.comunicacao.sistema.enumeracoes.Perfil;
import com.comunicacao.sistema.hateoas.AdicionadorLinkUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class ControleUsuario {

	@Autowired
	private AdicionadorLinkUsuario adicionadorLinkUsuario;

	private static final String BASE_URL = "http://localhost:8080";

	@GetMapping("/funcionarios/{empresaId}")
	public ResponseEntity<?> obterUsuarios(@PathVariable("empresaId") Long empresaId,
										   @RequestHeader(value = "Authorization", required = false) String token) {
		if (isInvalidToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido ou ausente.");
		}

		String url = buildUrl("/usuario/get/all", empresaId);
		HttpEntity<Void> entity = new HttpEntity<>(createHeaders(token));

		try {
			ResponseEntity<List<UsuarioDTO>> response = executeRequest(url, entity, new ParameterizedTypeReference<>() {});

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				List<UsuarioDTO> usuariosFiltrados = filtrarFuncionariosPorEmpresa(response.getBody(), empresaId);
				var responseDTO = UsuarioResponseDTO.from(usuariosFiltrados);

				adicionadorLinkUsuario.adicionarLink(responseDTO, token, empresaId);

				return ResponseEntity.ok(responseDTO);
			}
			return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
		} catch (HttpClientErrorException e) {
			return handleHttpClientError(e);
		}
	}

	@GetMapping("/funcionarios/{empresaId}/unique/{usuarioId}")
	public ResponseEntity<?> obterUsuario(@PathVariable("empresaId") Long empresaId,
										  @PathVariable("usuarioId") Long usuarioId,
										  @RequestHeader(value = "Authorization", required = false) String token) {
		if (isInvalidToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido ou ausente.");
		}

		String url = buildUrl("/usuario/get/unique/{usuarioId}", usuarioId);
		HttpEntity<Void> entity = new HttpEntity<>(createHeaders(token));

		try {
			ResponseEntity<UsuarioDTO> response = executeRequest(url, entity, new ParameterizedTypeReference<>() {});

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				UsuarioDTO usuarioDTO = response.getBody();

				if (!isFuncionarioDaEmpresa(usuarioDTO, empresaId)) {
					return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Usuário não pertence à empresa ou não é funcionário.");
				}

				var responseDTO = UsuarioResponseDTO.from(usuarioDTO);
				adicionadorLinkUsuario.adicionarLink(responseDTO, token, empresaId);

				return ResponseEntity.ok(responseDTO);
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
		} catch (HttpClientErrorException e) {
			return handleHttpClientError(e);
		}
	}

	// Métodos Privados

	private boolean isInvalidToken(String token) {
		return token == null || !token.startsWith("Bearer ");
	}

	private HttpHeaders createHeaders(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token.replace("Bearer ", ""));
		return headers;
	}

	private <T> ResponseEntity<T> executeRequest(String url, HttpEntity<?> entity, ParameterizedTypeReference<T> responseType) {
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.exchange(url, HttpMethod.GET, entity, responseType);
	}

	private String buildUrl(String endpoint, Object... uriVariables) {
		return UriComponentsBuilder.fromHttpUrl(BASE_URL + endpoint)
				.buildAndExpand(uriVariables)
				.toUriString();
	}

	private List<UsuarioDTO> filtrarFuncionariosPorEmpresa(List<UsuarioDTO> usuarios, Long empresaId) {
		return usuarios.stream()
				.filter(u -> u.getEmpresa() != null
						&& u.getEmpresa().getId().equals(empresaId)
						&& verificarFuncionario(u))
				.toList();
	}

	private boolean isFuncionarioDaEmpresa(UsuarioDTO usuario, Long empresaId) {
		return usuario.getEmpresa() != null
				&& usuario.getEmpresa().getId().equals(empresaId)
				&& verificarFuncionario(usuario);
	}

	private boolean verificarFuncionario(UsuarioDTO usuario) {
		return usuario.getPerfis().contains(Perfil.ROLE_CLIENTE) &&
				usuario.getPerfis().stream().anyMatch(perfil -> perfil != Perfil.ROLE_CLIENTE);
	}

	private ResponseEntity<String> handleHttpClientError(HttpClientErrorException e) {
		return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
	}
}
