package com.comunicacao.sistema.hateoas;

import com.comunicacao.sistema.controles.ControleUsuario;
import com.comunicacao.sistema.dtos.responseDTO.UsuarioResponseDTO;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<UsuarioResponseDTO> {

    @Override
    public void adicionarLink(List<UsuarioResponseDTO> lista, String token, Long empresaId) {
        for (UsuarioResponseDTO usuario : lista) {
            long id = usuario.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(ControleUsuario.class)
                            .obterUsuario(empresaId,id, token))
                    .withSelfRel();
            usuario.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(UsuarioResponseDTO objeto,  String token, Long empresaId) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(ControleUsuario.class)
                        .obterUsuarios(empresaId, token))
                .withRel("funcionarios");
        objeto.add(linkProprio);
    }
}
