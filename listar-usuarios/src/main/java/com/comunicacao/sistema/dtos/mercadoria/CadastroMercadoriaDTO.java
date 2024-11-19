package com.comunicacao.sistema.dtos.mercadoria;

import lombok.Data;

import java.util.Date;

@Data
public class CadastroMercadoriaDTO {
    private Date validade;
    private Date fabricao;
    private Date cadastro;
    private String nome;
    private long quantidade;
    private double valor;
    private String descricao;
    private long empresaId;
}
