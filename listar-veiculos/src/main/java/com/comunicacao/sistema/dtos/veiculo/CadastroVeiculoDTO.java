package com.comunicacao.sistema.dtos.veiculo;

import com.comunicacao.sistema.enumeracoes.TipoVeiculo;
import lombok.Data;

@Data
public class CadastroVeiculoDTO {
    private TipoVeiculo tipo;
    private String modelo;
    private String placa;
    private Long idProprietario;
}
