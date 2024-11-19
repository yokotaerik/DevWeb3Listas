package com.comunicacao.sistema.dtos.veiculo;

import com.comunicacao.sistema.dtos.usuario.UsuarioSimplesDTO;
import com.comunicacao.sistema.entidades.Veiculo;
import com.comunicacao.sistema.enumeracoes.TipoVeiculo;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class VeiculoComDonoDTO extends RepresentationModel<VeiculoComDonoDTO> {
    private Long id;
    private TipoVeiculo tipo;
    private String modelo;
    private String placa;
    private UsuarioSimplesDTO proprietario;

    public static VeiculoComDonoDTO from(Veiculo veiculo) {
        VeiculoComDonoDTO dto = new VeiculoComDonoDTO();
        dto.setId(veiculo.getId());
        dto.setTipo(veiculo.getTipo());
        dto.setModelo(veiculo.getModelo());
        dto.setPlaca(veiculo.getPlaca());
        dto.setProprietario(UsuarioSimplesDTO.from(veiculo.getProprietario()));
        return dto;
    }

    public static List<VeiculoComDonoDTO> from(List<Veiculo> veiculos) {
        return veiculos.stream().map(VeiculoComDonoDTO::from).collect(Collectors.toList());
    }
}
