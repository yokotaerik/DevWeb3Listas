package com.autobots.automanager.dtos.usuario;

import com.autobots.automanager.entidades.Usuario;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UsuarioSimplesDTO {
    private Long id;
    private String nome;

    public static UsuarioSimplesDTO from(Usuario usuario) {
        UsuarioSimplesDTO usuarioSimplesDTO = new UsuarioSimplesDTO();
        usuarioSimplesDTO.setId(usuario.getId());
        usuarioSimplesDTO.setNome(usuario.getNome());
        return usuarioSimplesDTO;
    }

    public static List<UsuarioSimplesDTO> from(Set<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioSimplesDTO::from).collect(Collectors.toList());
    }
}
