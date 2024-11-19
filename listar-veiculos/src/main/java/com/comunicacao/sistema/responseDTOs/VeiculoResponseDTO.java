package com.comunicacao.sistema.responseDTOs;

import com.comunicacao.sistema.dtos.usuario.UsuarioSimplesDTO;
import com.comunicacao.sistema.dtos.veiculo.VeiculoComDonoDTO;
import com.comunicacao.sistema.enumeracoes.TipoVeiculo;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class VeiculoResponseDTO extends RepresentationModel<VeiculoResponseDTO> {
    private Long id;
    private TipoVeiculo tipo;
    private String modelo;
    private String placa;
    private UsuarioSimplesDTO proprietario;

    public static VeiculoResponseDTO from(VeiculoComDonoDTO veiculo) {
        VeiculoResponseDTO dto = new VeiculoResponseDTO();
        dto.setId(veiculo.getId());
        dto.setTipo(veiculo.getTipo());
        dto.setModelo(veiculo.getModelo());
        dto.setPlaca(veiculo.getPlaca());
        dto.setProprietario(veiculo.getProprietario());
        return dto;
    }
}
