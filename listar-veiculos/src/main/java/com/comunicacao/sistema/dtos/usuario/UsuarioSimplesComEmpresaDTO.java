package com.comunicacao.sistema.dtos.usuario;

import com.comunicacao.sistema.dtos.empresa.EmpresaSimplesDTO;
import lombok.Data;

@Data
public class UsuarioSimplesComEmpresaDTO
{
    private Long id;
    private String nome;
    private EmpresaSimplesDTO empresa;
}
