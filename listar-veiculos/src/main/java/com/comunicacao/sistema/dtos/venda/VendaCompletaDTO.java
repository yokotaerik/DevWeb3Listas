package com.comunicacao.sistema.dtos.venda;

import com.comunicacao.sistema.dtos.mercadoria.MercadoriaSimplesDTO;
import com.comunicacao.sistema.dtos.servico.ServicoSimplesDTO;
import com.comunicacao.sistema.dtos.usuario.UsuarioSimplesComEmpresaDTO;
import com.comunicacao.sistema.dtos.usuario.UsuarioSimplesDTO;
import com.comunicacao.sistema.dtos.veiculo.VeiculoComDonoDTO;
import com.comunicacao.sistema.entidades.Venda;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class VendaCompletaDTO extends RepresentationModel<VendaCompletaDTO> {
    private Long id;
    private Date cadastro;
    private String identificacao;
    private List<MercadoriaSimplesDTO> mercadorias;
    private List<ServicoSimplesDTO> servicos;
    private VeiculoComDonoDTO veiculo;
    private UsuarioSimplesDTO cliente;
    private UsuarioSimplesComEmpresaDTO funcionario;
}
