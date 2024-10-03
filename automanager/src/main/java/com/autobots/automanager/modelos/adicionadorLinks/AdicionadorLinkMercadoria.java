package com.autobots.automanager.modelos.adicionadorLinks;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.entidades.Mercadoria;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria> {

	@Override
	public void adicionarLink(List<Mercadoria> lista) {
		for (Mercadoria servico : lista) {
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
	public void adicionarLink(Mercadoria objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(MercadoriaControle.class)
						.obterMercadorias())
				.withRel("servicos");
		objeto.add(linkProprio);
	}
}