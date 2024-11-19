package com.comunicacao.sistema.controles;

import java.util.List;
import java.util.Objects;

import com.comunicacao.sistema.dtos.mercadoria.MercadoriaCompletaDTO;
import com.comunicacao.sistema.hateoas.AdicionadorLinkMercadoria;
import com.comunicacao.sistema.responseDTOs.MercadoriaResponseDTO;
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
public class MercadoriaController {

	@Autowired
	private AdicionadorLinkMercadoria adicionadorLink;

	private static final String BASE_URL = "http://localhost:8080";

	@GetMapping("/mercadorias/{empresaId}")
	public ResponseEntity<?> obterMercadorias(@PathVariable Long empresaId,
											  @RequestHeader(value = "Authorization", required = false) String token) {
		if (isInvalidToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido ou ausente.");
		}

		String url = buildUrl("/mercadoria/get/all");
		HttpEntity<Void> entity = new HttpEntity<>(createHeaders(token));

		try {
			ResponseEntity<List<MercadoriaCompletaDTO>> response = executeRequest(
					url, entity, new ParameterizedTypeReference<List<MercadoriaCompletaDTO>>() {});

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				List<MercadoriaCompletaDTO> dadosFiltrados = filterByEmpresaId(response.getBody(), empresaId);

				var dtos = MercadoriaResponseDTO.from(dadosFiltrados);
				adicionadorLink.adicionarLink(dtos, token, empresaId);

				return ResponseEntity.ok(dtos);
			}
			return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
		} catch (HttpClientErrorException e) {
			return handleHttpClientError(e);
		}
	}

	@GetMapping("/mercadorias/{empresaId}/unique/{mercadoriaId}")
	public ResponseEntity<?> obterMercadoriaUnica(@PathVariable Long empresaId,
												  @PathVariable Long mercadoriaId,
												  @RequestHeader(value = "Authorization", required = false) String token) {
		if (isInvalidToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido ou ausente.");
		}

		String url = buildUrl("/mercadoria/get/unique/{mercadoriaId}", mercadoriaId);
		HttpEntity<Void> entity = new HttpEntity<>(createHeaders(token));

		try {
			ResponseEntity<MercadoriaCompletaDTO> response = executeRequest(
					url, entity, new ParameterizedTypeReference<MercadoriaCompletaDTO>() {});

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				MercadoriaCompletaDTO mercadoria = response.getBody();
				if (isMercadoriaDaEmpresa(mercadoria, empresaId)) {

					var dto = MercadoriaResponseDTO.from(mercadoria);
					adicionadorLink.adicionarLink(dto, token, empresaId);

					return ResponseEntity.ok(dto);
				}
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("A mercadoria não pertence à empresa especificada.");
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mercadoria não encontrada");
		} catch (HttpClientErrorException e) {
			return handleHttpClientError(e);
		}
	}

	/// MÉTODOS PRIVADOS

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

	private List<MercadoriaCompletaDTO> filterByEmpresaId(List<MercadoriaCompletaDTO> mercadorias, Long empresaId) {
		return mercadorias.stream()
				.filter(m -> m.getEmpresa() != null && Objects.equals(m.getEmpresa().getId(), empresaId))
				.toList();
	}

	private boolean isMercadoriaDaEmpresa(MercadoriaCompletaDTO mercadoria, Long empresaId) {
		return mercadoria.getEmpresa() != null && Objects.equals(mercadoria.getEmpresa().getId(), empresaId);
	}

	private ResponseEntity<String> handleHttpClientError(HttpClientErrorException e) {
		return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
	}
}
