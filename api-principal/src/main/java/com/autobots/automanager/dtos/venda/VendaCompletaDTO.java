package com.autobots.automanager.dtos.venda;

import com.autobots.automanager.dtos.mercadoria.MercadoriaSimplesDTO;
import com.autobots.automanager.dtos.servico.ServicoSimplesDTO;
import com.autobots.automanager.dtos.usuario.UsuarioSimplesComEmpresaDTO;
import com.autobots.automanager.dtos.usuario.UsuarioSimplesDTO;
import com.autobots.automanager.dtos.veiculo.VeiculoComDonoDTO;
import com.autobots.automanager.entidades.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Collection;
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

    public VendaCompletaDTO() {
    }

    public VendaCompletaDTO(long id, Date cadastro, String identificacao, Usuario cliente, Usuario funcionario, Veiculo veiculo, Collection<Mercadoria> mercadorias, Collection<Servico> servicos) {
        this.id = id;
        this.cadastro = cadastro;
        this.identificacao = identificacao;
        this.cliente = UsuarioSimplesDTO.from(cliente);
        this.funcionario = UsuarioSimplesComEmpresaDTO.from(funcionario);
        this.veiculo = VeiculoComDonoDTO.from(veiculo);
        this.mercadorias = MercadoriaSimplesDTO.from(mercadorias.stream().toList());
        this.servicos = ServicoSimplesDTO.from(servicos.stream().toList());
    }

    public static VendaCompletaDTO fromEntity(Venda venda) {
        VendaCompletaDTO dto = new VendaCompletaDTO();
        dto.setId(venda.getId());
        dto.setCadastro(venda.getCadastro());
        dto.setIdentificacao(venda.getIdentificacao());
        dto.setMercadorias(MercadoriaSimplesDTO.from(venda.getMercadorias()));
        dto.setServicos(ServicoSimplesDTO.from(venda.getServicos()));
        dto.setVeiculo(VeiculoComDonoDTO.from(venda.getVeiculo()));
        dto.setCliente(UsuarioSimplesDTO.from(venda.getCliente()));
        dto.setFuncionario(UsuarioSimplesComEmpresaDTO.from(venda.getFuncionario()));
        return dto;
    }

    public static List<VendaCompletaDTO> from(List<Venda> vendas) {
        return vendas.stream().map(VendaCompletaDTO::fromEntity).collect(Collectors.toList());
    }
}
