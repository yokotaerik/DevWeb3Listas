package com.comunicacao.sistema.dtos.mercadoria;

import com.comunicacao.sistema.dtos.empresa.EmpresaSimplesDTO;
import lombok.Data;

import java.util.Date;

@Data
public class MercadoriaCompletaDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private long quantidade;
    private Date cadastro;
    private Date validade;
    private Date fabricacao;
    private EmpresaSimplesDTO empresa;

}
