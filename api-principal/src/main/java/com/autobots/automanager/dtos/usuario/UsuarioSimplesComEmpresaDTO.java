
package com.autobots.automanager.dtos.usuario;

import com.autobots.automanager.dtos.empresa.EmpresaSimplesDTO;
import com.autobots.automanager.entidades.Usuario;
import lombok.Data;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class UsuarioSimplesComEmpresaDTO
{
    private Long id;
    private String nome;
    private EmpresaSimplesDTO empresa;

    public static UsuarioSimplesComEmpresaDTO from(Usuario usuario) {
        UsuarioSimplesComEmpresaDTO usuarioSimplesDTO = new UsuarioSimplesComEmpresaDTO();
        usuarioSimplesDTO.setId(usuario.getId());
        usuarioSimplesDTO.setNome(usuario.getNome());
        usuarioSimplesDTO.setEmpresa(EmpresaSimplesDTO.from(usuario.getEmpresa()));
        return usuarioSimplesDTO;
    }

    public static List<UsuarioSimplesComEmpresaDTO> from(Set<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioSimplesComEmpresaDTO::from).collect(Collectors.toList());
    }
}
