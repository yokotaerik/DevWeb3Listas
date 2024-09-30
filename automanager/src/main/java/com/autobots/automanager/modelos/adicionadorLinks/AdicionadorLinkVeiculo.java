package com.autobots.automanager.modelos.adicionadorLinks;

import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.dtos.veiculo.VeiculoComDonoDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<VeiculoComDonoDTO> {

	@Override
	public void adicionarLink(List<VeiculoComDonoDTO> lista) {
		for (VeiculoComDonoDTO veiculo : lista) {
			long id = veiculo.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(VeiculoControle.class)
							.obterVeiculo(id))
					.withSelfRel();
			veiculo.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(VeiculoComDonoDTO objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(VeiculoControle.class)
						.obterVeiculos())
				.withRel("veiculos");
		objeto.add(linkProprio);
	}
}