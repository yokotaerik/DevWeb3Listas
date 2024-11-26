package com.autobots.automanager.dtos.documento;

import lombok.Data;

import java.util.Date;

@Data
public class CadastroDocumentoDTO {

    private String tipo;
    private String numero;
    private Long clienteId;

}
