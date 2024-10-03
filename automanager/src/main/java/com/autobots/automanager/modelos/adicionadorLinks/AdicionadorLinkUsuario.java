package com.autobots.automanager.modelos.adicionadorLinks;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.dtos.usuario.UsuarioDTO;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Usuario;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<UsuarioDTO> {

    @Override
    public void adicionarLink(List<UsuarioDTO> lista) {
        for (UsuarioDTO usuario : lista) {
            long id = usuario.getId();
            Link linkProprio = WebMvcLinkBuilder
                    .linkTo(WebMvcLinkBuilder
                            .methodOn(UsuarioControle.class)
                            .obterUsuario(id))
                    .withSelfRel();
            usuario.add(linkProprio);
        }
    }

    @Override
    public void adicionarLink(UsuarioDTO objeto) {
        Link linkProprio = WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder
                        .methodOn(UsuarioControle.class)
                        .obterUsuarios())
                .withRel("usuarios");
        objeto.add(linkProprio);
    }
}