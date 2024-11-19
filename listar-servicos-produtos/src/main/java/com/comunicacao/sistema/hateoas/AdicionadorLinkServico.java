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
public class AdicionadorLinkServico implements AdicionadorLink<ServicoResponseDTO> {

    @Override
    public void adicionarLink(List<ServicoResponseDTO> lista, String token, Long empresaId) {
        for (ServicoResponseDTO servico : lista) {
            long id = servico.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ServicoController.class)
                            .obterServicoUnico(empresaId,id, token))
                    .withSelfRel();
            servico.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(ServicoResponseDTO objeto,  String token, Long empresaId) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ServicoController.class)
                        .obterServicos(empresaId, token))
                .withRel("servicos");
        objeto.add(linkProprio);
    }
}
