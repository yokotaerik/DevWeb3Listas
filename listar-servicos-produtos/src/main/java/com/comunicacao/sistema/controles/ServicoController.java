package com.comunicacao.sistema.controles;

import com.comunicacao.sistema.dtos.servico.ServicoSimplesDTO;
import com.comunicacao.sistema.hateoas.AdicionadorLinkServico;
import com.comunicacao.sistema.responseDTOs.ServicoResponseDTO;
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

import java.util.List;
import java.util.Objects;

@RestController
public class ServicoController {

	@Autowired
	private AdicionadorLinkServico adicionadorLink;

	private static final String BASE_URL = "http://localhost:8080";

	@GetMapping("/servicos/{empresaId}")
	public ResponseEntity<?> obterServicos(@PathVariable("empresaId") Long empresaId,
										   @RequestHeader(value = "Authorization", required = false) String token) {

		if (isInvalidToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido ou ausente.");
		}

		String url = buildUrl("/servico/get/all");
		HttpEntity<Void> entity = new HttpEntity<>(createHeaders(token));

		try {
			ResponseEntity<List<ServicoSimplesDTO>> response = executeRequest(
					url, entity, new ParameterizedTypeReference<List<ServicoSimplesDTO>>() {});

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				List<ServicoSimplesDTO> dadosFiltrados = filterByEmpresaId(response.getBody(), empresaId);
				var dtos = ServicoResponseDTO.from(dadosFiltrados);
				adicionadorLink.adicionarLink(dtos, token, empresaId);

				return ResponseEntity.ok(dtos);
			}
			return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

		} catch (HttpClientErrorException e) {
			return handleHttpClientError(e);
		}
	}

	@GetMapping("/servicos/{empresaId}/unique/{servicoId}")
	public ResponseEntity<?> obterServicoUnico(@PathVariable Long empresaId,
											   @PathVariable Long servicoId,
											   @RequestHeader(value = "Authorization", required = false) String token) {

		if (isInvalidToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido ou ausente.");
		}

		String url = buildUrl("/servico/get/unique/{servicoId}", servicoId);
		HttpEntity<Void> entity = new HttpEntity<>(createHeaders(token));

		try {
			ResponseEntity<ServicoSimplesDTO> response = executeRequest(
					url, entity, new ParameterizedTypeReference<ServicoSimplesDTO>() {});

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				ServicoSimplesDTO servico = response.getBody();
				if (isServicoDaEmpresa(servico, empresaId)) {
					var dto = ServicoResponseDTO.from(servico);
					adicionadorLink.adicionarLink(dto, token, empresaId);
					return ResponseEntity.ok(dto);
				}
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("O serviço não pertence à empresa especificada.");
			}
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Serviço não encontrado");

		} catch (HttpClientErrorException e) {
			return handleHttpClientError(e);
		}
	}

	// MÉTODOS PRIVADOS

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

	private List<ServicoSimplesDTO> filterByEmpresaId(List<ServicoSimplesDTO> servicos, Long empresaId) {
		return servicos.stream()
				.filter(s -> s.getEmpresa() != null && Objects.equals(s.getEmpresa().getId(), empresaId))
				.toList();
	}

	private boolean isServicoDaEmpresa(ServicoSimplesDTO servico, Long empresaId) {
		return servico.getEmpresa() != null && Objects.equals(servico.getEmpresa().getId(), empresaId);
	}

	private ResponseEntity<String> handleHttpClientError(HttpClientErrorException e) {
		return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
	}
}
