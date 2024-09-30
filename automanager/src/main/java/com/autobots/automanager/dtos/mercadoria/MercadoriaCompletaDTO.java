package com.autobots.automanager.dtos.mercadoria;

import com.autobots.automanager.entidades.Mercadoria;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static MercadoriaCompletaDTO from(Mercadoria mercadoria){
        MercadoriaCompletaDTO dto = new MercadoriaCompletaDTO();
        dto.setId(mercadoria.getId());
        dto.setNome(mercadoria.getNome());
        dto.setDescricao(mercadoria.getDescricao());
        dto.setPreco(mercadoria.getValor());
        dto.setQuantidade(mercadoria.getQuantidade());
        dto.setCadastro(mercadoria.getCadastro());
        dto.setValidade(mercadoria.getValidade());
        dto.setFabricacao(mercadoria.getFabricao());
        return dto;
    }

    public static List<MercadoriaCompletaDTO> from(Set<Mercadoria> mercadorias){
        return mercadorias.stream().map(MercadoriaCompletaDTO::from).collect(Collectors.toList());
    }
}
