package com.comunicacao.sistema.responseDTOs;

import com.comunicacao.sistema.dtos.empresa.EmpresaSimplesDTO;
import com.comunicacao.sistema.dtos.mercadoria.MercadoriaCompletaDTO;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.List;

@Data
public class MercadoriaResponseDTO extends RepresentationModel<MercadoriaResponseDTO> {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private long quantidade;
    private Date cadastro;
    private Date validade;
    private Date fabricacao;
    private EmpresaSimplesDTO empresa;

    public static MercadoriaResponseDTO from(MercadoriaCompletaDTO dto){
        MercadoriaResponseDTO response = new MercadoriaResponseDTO();
        response.setId(dto.getId());
        response.setNome(dto.getNome());
        response.setDescricao(dto.getDescricao());
        response.setPreco(dto.getPreco());
        response.setQuantidade(dto.getQuantidade());
        response.setCadastro(dto.getCadastro());
        response.setValidade(dto.getValidade());
        response.setFabricacao(dto.getFabricacao());
        response.setEmpresa(dto.getEmpresa());
        return response;
    }

    public static List<MercadoriaResponseDTO> from(List<MercadoriaCompletaDTO> dtos){
        return dtos.stream().map(MercadoriaResponseDTO::from).toList();
    }
}
