package com.autobots.automanager.dtos.venda;

import com.autobots.automanager.dtos.mercadoria.MercadoriaSimplesDTO;
import com.autobots.automanager.dtos.servico.ServicoSimplesDTO;
import com.autobots.automanager.dtos.usuario.UsuarioSimplesDTO;
import com.autobots.automanager.dtos.veiculo.VeiculoComDonoDTO;
import com.autobots.automanager.dtos.veiculo.VeiculoDTO;
import com.autobots.automanager.entidades.Venda;
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
    private UsuarioSimplesDTO funcionario;

    public static VendaCompletaDTO fromEntity(Venda venda) {
        VendaCompletaDTO dto = new VendaCompletaDTO();
        dto.setId(venda.getId());
        dto.setCadastro(venda.getCadastro());
        dto.setIdentificacao(venda.getIdentificacao());
        dto.setMercadorias(MercadoriaSimplesDTO.from(venda.getMercadorias()));
        dto.setServicos(ServicoSimplesDTO.from(venda.getServicos()));
        dto.setVeiculo(VeiculoComDonoDTO.from(venda.getVeiculo()));
        dto.setCliente(UsuarioSimplesDTO.from(venda.getCliente()));
        dto.setFuncionario(UsuarioSimplesDTO.from(venda.getFuncionario()));
        return dto;
    }

    public static List<VendaCompletaDTO> from(List<Venda> vendas) {
        return vendas.stream().map(VendaCompletaDTO::fromEntity).collect(Collectors.toList());
    }
}
