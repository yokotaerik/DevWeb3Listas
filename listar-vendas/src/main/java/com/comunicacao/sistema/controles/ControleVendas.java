package com.comunicacao.sistema.controles;

import java.util.List;

import com.comunicacao.sistema.dtos.venda.VendaCompletaDTO;
import com.comunicacao.sistema.hateoas.AdicionadorLinkVenda;
import com.comunicacao.sistema.responseDTOs.ResponseVendaDTO;
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
public class ControleVendas {

	@Autowired
	private AdicionadorLinkVenda adicionarLink;

	private static final String BASE_URL = "http://localhost:8080";

	@GetMapping("/vendas/{empresaId}")
	public ResponseEntity<?> obterVendas(@PathVariable("empresaId") Long empresaId,
										 @RequestHeader(value = "Authorization", required = false) String token) {

		if (isInvalidToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido ou ausente.");
		}

		String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/venda/get/all").toUriString();
		HttpHeaders headers = createHeaders(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<List<VendaCompletaDTO>> response = executeRequest(
					url, entity, new ParameterizedTypeReference<List<VendaCompletaDTO>>() {});

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				var vendasFiltradas = response.getBody().stream()
						.filter(v -> v.getFuncionario() != null && v.getFuncionario().getEmpresa().getId().equals(empresaId))
						.toList();

				var dtos = ResponseVendaDTO.from(vendasFiltradas);
				adicionarLink.adicionarLink(dtos, token, empresaId);

				return ResponseEntity.ok(dtos);
			} else {
				return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
			}
		} catch (HttpClientErrorException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		}
	}

	@GetMapping("/vendas/{empresaId}/unique/{vendaId}")
	public ResponseEntity<?> obterVendaUnica(@PathVariable("empresaId") Long empresaId,
											 @PathVariable("vendaId") Long vendaId,
											 @RequestHeader(value = "Authorization", required = false) String token) {

		if (isInvalidToken(token)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token JWT inválido ou ausente.");
		}

		String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/venda/get/unique/{vendaId}")
				.buildAndExpand(vendaId)
				.toUriString();

		HttpHeaders headers = createHeaders(token);
		HttpEntity<Void> entity = new HttpEntity<>(headers);

		try {
			ResponseEntity<VendaCompletaDTO> response = executeRequest(
					url, entity, new ParameterizedTypeReference<VendaCompletaDTO>() {});

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				var dto = ResponseVendaDTO.from(response.getBody());
				adicionarLink.adicionarLink(dto, empresaId, token);
				return ResponseEntity.ok(dto);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Venda não encontrada");
			}
		} catch (HttpClientErrorException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
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
}
