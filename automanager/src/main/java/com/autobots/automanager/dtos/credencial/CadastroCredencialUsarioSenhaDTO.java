package com.autobots.automanager.dtos.credencial;

import lombok.Data;

@Data
public class CadastroCredencialUsarioSenhaDTO {
    private String usuario;
    private String senha;
    private Long usuarioId;
}
