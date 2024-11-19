package com.comunicacao.sistema.responseDTOs;

import com.comunicacao.sistema.dtos.empresa.EmpresaSimplesDTO;
import com.comunicacao.sistema.dtos.servico.ServicoSimplesDTO;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class ServicoResponseDTO extends RepresentationModel<ServicoResponseDTO> {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private EmpresaSimplesDTO empresa;

    public static ServicoResponseDTO from(ServicoSimplesDTO dto){
        ServicoResponseDTO response = new ServicoResponseDTO();
        response.setId(dto.getId());
        response.setNome(dto.getNome());
        response.setDescricao(dto.getDescricao());
        response.setPreco(dto.getPreco());
        response.setEmpresa(dto.getEmpresa());
        return response;
    }

    public static List<ServicoResponseDTO> from(List<ServicoSimplesDTO> dtos){
        return dtos.stream().map(ServicoResponseDTO::from).toList();
    }

}

