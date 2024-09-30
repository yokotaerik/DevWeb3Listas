package com.autobots.automanager.dtos.servico;

import com.autobots.automanager.entidades.Servico;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ServicoSimplesDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;


    public static ServicoSimplesDTO from(Servico servico) {
        ServicoSimplesDTO servicoSimplesDTO = new ServicoSimplesDTO();
        servicoSimplesDTO.setId(servico.getId());
        servicoSimplesDTO.setNome(servico.getNome());
        servicoSimplesDTO.setDescricao(servico.getDescricao());
        servicoSimplesDTO.setPreco(servico.getValor());
        return servicoSimplesDTO;
    }

    public static List<ServicoSimplesDTO> from(List<Servico> servicos) {
        return servicos.stream().map(ServicoSimplesDTO::from).collect(Collectors.toList());
    }
}
