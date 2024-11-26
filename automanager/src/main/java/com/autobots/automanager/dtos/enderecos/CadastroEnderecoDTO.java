package com.autobots.automanager.dtos.enderecos;

import lombok.Data;

@Data
public class CadastroEnderecoDTO {
    private String estado;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String codigoPostal;
    private String informacoesAdicionais;
    private long clienteId;
}
