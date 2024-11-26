package com.autobots.automanager.dtos.telefone;

import lombok.Data;

@Data
public class CadastroTelefoneDTO {
    private String ddd;
    private String numero;
    private long clienteId;
}
