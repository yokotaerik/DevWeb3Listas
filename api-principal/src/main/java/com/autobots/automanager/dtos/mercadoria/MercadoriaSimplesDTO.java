package com.autobots.automanager.dtos.mercadoria;

import com.autobots.automanager.dtos.empresa.EmpresaSimplesDTO;
import com.autobots.automanager.entidades.Mercadoria;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class MercadoriaSimplesDTO {
    private Long id;
    private String nome;
    private Double preco;
    private EmpresaSimplesDTO empresa;

    public static MercadoriaSimplesDTO from(Mercadoria mercadoria) {
        MercadoriaSimplesDTO mercadoriaSimplesDTO = new MercadoriaSimplesDTO();
        mercadoriaSimplesDTO.setId(mercadoria.getId());
        mercadoriaSimplesDTO.setNome(mercadoria.getNome());
        mercadoriaSimplesDTO.setPreco(mercadoria.getValor());
        mercadoriaSimplesDTO.setEmpresa(EmpresaSimplesDTO.from(mercadoria.getEmpresa()));
        return mercadoriaSimplesDTO;
    }

    public static List<MercadoriaSimplesDTO> from(List<Mercadoria> mercadorias) {
        return mercadorias.stream().map(MercadoriaSimplesDTO::from).collect(Collectors.toList());
    }
}
