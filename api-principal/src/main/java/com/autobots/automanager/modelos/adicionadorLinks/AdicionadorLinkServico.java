package com.autobots.automanager.modelos.adicionadorLinks;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.dtos.servico.ServicoSimplesDTO;
import com.autobots.automanager.entidades.Servico;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<ServicoSimplesDTO> {

	@Override
	public void adicionarLink(List<ServicoSimplesDTO> lista) {
		for (ServicoSimplesDTO servico : lista) {
			long id = servico.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(ServicoControle.class)
							.obterServico(id))
					.withSelfRel();
			servico.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(ServicoSimplesDTO objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(ServicoControle.class)
						.obterServicos())
				.withRel("servicos");
		objeto.add(linkProprio);
	}
}