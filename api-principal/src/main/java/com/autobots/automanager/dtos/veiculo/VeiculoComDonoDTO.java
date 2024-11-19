package com.autobots.automanager.dtos.veiculo;

import com.autobots.automanager.dtos.usuario.UsuarioSimplesDTO;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.enumeracoes.TipoVeiculo;
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
