package com.autobots.automanager.modelos.adicionadorLinks;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.dtos.empresa.EmpresaCompletaDTO;
import com.autobots.automanager.entidades.Empresa;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkEmpresa implements AdicionadorLink<EmpresaCompletaDTO> {

	@Override
	public void adicionarLink(List<EmpresaCompletaDTO> lista) {
		for (EmpresaCompletaDTO telefone : lista) {
			long id = telefone.getId();
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder
							.methodOn(EmpresaControle.class)
							.obterEmpresa(id))
					.withSelfRel();
			telefone.add(linkProprio);
		}
	}

	@Override
	public void adicionarLink(EmpresaCompletaDTO objeto) {
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder
						.methodOn(EmpresaControle.class)
						.obterEmpresas())
				.withRel("empresas");
		objeto.add(linkProprio);
	}
}