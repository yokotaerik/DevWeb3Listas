package com.comunicacao.sistema.dtos.mercadoria;

import com.comunicacao.sistema.dtos.empresa.EmpresaSimplesDTO;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MercadoriaSimplesDTO {
    private Long id;
    private String nome;
    private Double preco;
    private EmpresaSimplesDTO empresa;


}
