package com.autobots.automanager.modelos.adicionadorLinks;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.dtos.mercadoria.MercadoriaCompletaDTO;
import com.autobots.automanager.entidades.Mercadoria;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<MercadoriaCompletaDTO> {

	@Override
	public void adicionarLink(List<MercadoriaCompletaDTO> lista) {
		for (MercadoriaCompletaDTO servico : lista) {
			long id = servico.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(MercadoriaControle.class)
							.obterMercadoria(id))
					.withSelfRel();
			servico.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(MercadoriaCompletaDTO objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(MercadoriaControle.class)
						.obterMercadorias())
				.withRel("mercadorias");
		objeto.add(linkProprio);
	}
}