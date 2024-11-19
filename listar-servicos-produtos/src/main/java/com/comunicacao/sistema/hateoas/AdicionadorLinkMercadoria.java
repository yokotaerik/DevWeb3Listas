package com.comunicacao.sistema.hateoas;

import com.comunicacao.sistema.controles.MercadoriaController;
import com.comunicacao.sistema.controles.ServicoController;
import com.comunicacao.sistema.responseDTOs.MercadoriaResponseDTO;
import com.comunicacao.sistema.responseDTOs.ServicoResponseDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<MercadoriaResponseDTO> {

    @Override
    public void adicionarLink(List<MercadoriaResponseDTO> lista, String token, Long empresaId) {
        for (MercadoriaResponseDTO servico : lista) {
            long id = servico.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(MercadoriaController.class)
                            .obterMercadoriaUnica(empresaId,id, token))
                    .withSelfRel();
            servico.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(MercadoriaResponseDTO objeto,  String token, Long empresaId) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(MercadoriaController.class)
                        .obterMercadorias(empresaId, token))
                .withRel("mercadorias");
        objeto.add(linkProprio);
    }
}
