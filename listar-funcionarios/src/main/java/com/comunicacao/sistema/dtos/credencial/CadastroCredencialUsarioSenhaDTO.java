package com.comunicacao.sistema.dtos.credencial;

import lombok.Data;

@Data
public class CadastroCredencialUsarioSenhaDTO {
    private String usuario;
    private String senha;
    private Long usuarioId;
}
