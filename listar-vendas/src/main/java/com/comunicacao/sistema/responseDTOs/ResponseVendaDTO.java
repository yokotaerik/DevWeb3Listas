package com.comunicacao.sistema.responseDTOs;

import com.comunicacao.sistema.dtos.mercadoria.MercadoriaSimplesDTO;
import com.comunicacao.sistema.dtos.servico.ServicoSimplesDTO;
import com.comunicacao.sistema.dtos.usuario.UsuarioSimplesComEmpresaDTO;
import com.comunicacao.sistema.dtos.usuario.UsuarioSimplesDTO;
import com.comunicacao.sistema.dtos.veiculo.VeiculoComDonoDTO;
import com.comunicacao.sistema.dtos.venda.VendaCompletaDTO;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.List;

@Data
public class ResponseVendaDTO extends RepresentationModel<ResponseVendaDTO> {
    private Long id;
    private Date cadastro;
    private String identificacao;
    private List<MercadoriaSimplesDTO> mercadorias;
    private List<ServicoSimplesDTO> servicos;
    private VeiculoComDonoDTO veiculo;
    private UsuarioSimplesDTO cliente;
    private UsuarioSimplesComEmpresaDTO funcionario;

    public static ResponseVendaDTO from(VendaCompletaDTO dto){
        ResponseVendaDTO response = new ResponseVendaDTO();
        response.setId(dto.getId());
        response.setCadastro(dto.getCadastro());
        response.setIdentificacao(dto.getIdentificacao());
        response.setMercadorias(dto.getMercadorias());
        response.setServicos(dto.getServicos());
        response.setVeiculo(dto.getVeiculo());
        response.setCliente(dto.getCliente());
        response.setFuncionario(dto.getFuncionario());
        return response;
    }

    public static List<ResponseVendaDTO> from(List<VendaCompletaDTO> dtos){
        return dtos.stream().map(ResponseVendaDTO::from).toList();
    }
}

