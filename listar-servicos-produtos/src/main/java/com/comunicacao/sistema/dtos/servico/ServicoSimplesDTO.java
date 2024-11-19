package com.comunicacao.sistema.dtos.servico;

import com.comunicacao.sistema.dtos.empresa.EmpresaSimplesDTO;
import lombok.Data;

@Data
public class ServicoSimplesDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private EmpresaSimplesDTO empresa;
}
