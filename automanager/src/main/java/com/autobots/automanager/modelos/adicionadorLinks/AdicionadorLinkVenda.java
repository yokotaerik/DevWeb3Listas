package com.autobots.automanager.modelos.adicionadorLinks;

import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.dtos.venda.VendaCompletaDTO;
import com.autobots.automanager.entidades.Venda;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkVenda implements AdicionadorLink<VendaCompletaDTO> {

	@Override
	public void adicionarLink(List<VendaCompletaDTO> lista) {
		for (VendaCompletaDTO venda : lista) {
			long id = venda.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(VendaControle.class)
							.obterVenda(id))
					.withSelfRel();
			venda.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(VendaCompletaDTO objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VendaControle.class)
						.obterVendas())
				.withRel("vendas");
		objeto.add(linkProprio);
	}
}