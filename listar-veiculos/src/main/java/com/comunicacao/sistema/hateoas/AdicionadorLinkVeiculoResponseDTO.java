package com.comunicacao.sistema.hateoas;

import com.comunicacao.sistema.controles.ControleVeiculos;
import com.comunicacao.sistema.responseDTOs.VeiculoResponseDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkVeiculoResponseDTO implements AdicionadorLink<VeiculoResponseDTO> {

    @Override
    public void adicionarLink(List<VeiculoResponseDTO> lista, String token, Long empresaId) {
        for (VeiculoResponseDTO veiculo : lista) {
            long id = veiculo.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleVeiculos.class)
                            .obterVeiculo(empresaId,id, token))
                    .withSelfRel();
            veiculo.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(VeiculoResponseDTO objeto, Long empresaId, String token) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleVeiculos.class)
                        .obterVeiculos(empresaId, token))
                .withRel("veiculos");
        objeto.add(linkProprio);
    }
}
