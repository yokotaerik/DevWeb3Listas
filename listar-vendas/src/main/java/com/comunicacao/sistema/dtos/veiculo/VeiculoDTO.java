package com.comunicacao.sistema.dtos.veiculo;

import com.comunicacao.sistema.entidades.Veiculo;
import com.comunicacao.sistema.enumeracoes.TipoVeiculo;
import lombok.Data;

import java.util.List;

@Data
public class VeiculoDTO {
    private Long id;
    private TipoVeiculo tipo;
    private String modelo;
    private String placa;

    public static VeiculoDTO from(Veiculo veiculo) {
        VeiculoDTO dto = new VeiculoDTO();
        dto.setId(veiculo.getId());
        dto.setTipo(veiculo.getTipo());
        dto.setModelo(veiculo.getModelo());
        dto.setPlaca(veiculo.getPlaca());
        return dto;
    }

    public static List<VeiculoDTO> from(List<Veiculo> veiculos) {
        return veiculos.stream().map(VeiculoDTO::from).toList();
    }
}
