package com.autobots.automanager.dtos.servico;

import lombok.Data;

@Data
public class CadastroServicoDTO {
    private String nome;
    private double valor;
    private String descricao;
    private long empresaId;
}
