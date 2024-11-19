package com.comunicacao.sistema.hateoas;

import com.comunicacao.sistema.controles.ControleVendas;
import com.comunicacao.sistema.responseDTOs.ResponseVendaDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkVenda implements AdicionadorLink<ResponseVendaDTO> {

    @Override
    public void adicionarLink(List<ResponseVendaDTO> lista, String token, Long empresaId) {
        for (ResponseVendaDTO venda : lista) {
            long id = venda.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleVendas.class)
                            .obterVendaUnica(empresaId,id, token))
                    .withSelfRel();
            venda.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(ResponseVendaDTO objeto, Long empresaId, String token) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleVendas.class)
                        .obterVendas(empresaId, token))
                .withRel("vendas");
        objeto.add(linkProprio);
    }
}
