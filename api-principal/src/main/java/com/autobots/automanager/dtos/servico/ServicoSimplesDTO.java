package com.autobots.automanager.dtos.servico;

import com.autobots.automanager.dtos.empresa.EmpresaSimplesDTO;
import com.autobots.automanager.entidades.Servico;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ServicoSimplesDTO extends RepresentationModel<ServicoSimplesDTO> {
    private Long id;
    private String nome;
    private String descricao;
    private Double preco;
    private EmpresaSimplesDTO empresa;


    public static ServicoSimplesDTO from(Servico servico) {
        ServicoSimplesDTO servicoSimplesDTO = new ServicoSimplesDTO();
        servicoSimplesDTO.setId(servico.getId());
        servicoSimplesDTO.setNome(servico.getNome());
        servicoSimplesDTO.setDescricao(servico.getDescricao());
        servicoSimplesDTO.setPreco(servico.getValor());
        servicoSimplesDTO.setEmpresa(EmpresaSimplesDTO.from(servico.getEmpresa()));
        return servicoSimplesDTO;
    }

    public static List<ServicoSimplesDTO> from(List<Servico> servicos) {
        return servicos.stream().map(ServicoSimplesDTO::from).collect(Collectors.toList());
    }
}
