package com.comunicacao.sistema.dtos.venda;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CadastroVendaDTO {
    private Date cadastro;
    private String identificacao;
    private Long clienteId;
    private Long funcionarioId;
    private Long veiculoId;
    private List<Long> mercadoriasId;
    private List<Long> servicosId;
}
