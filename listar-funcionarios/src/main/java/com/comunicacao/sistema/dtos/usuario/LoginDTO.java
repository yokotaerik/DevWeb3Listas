package com.comunicacao.sistema.dtos.usuario;

import lombok.Data;

@Data
public class LoginDTO {
    private String nomeUsuario;
    private String senha;
}
