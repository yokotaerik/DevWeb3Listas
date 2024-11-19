package com.autobots.automanager.dtos.veiculo;

import com.autobots.automanager.enumeracoes.TipoVeiculo;
import lombok.Data;

@Data
public class CadastroVeiculoDTO {
    private TipoVeiculo tipo;
    private String modelo;
    private String placa;
    private Long idProprietario;
}
