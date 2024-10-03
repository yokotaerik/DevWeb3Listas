package com.autobots.automanager.dtos.empresa;

import com.autobots.automanager.dtos.mercadoria.MercadoriaCompletaDTO;
import com.autobots.automanager.dtos.servico.ServicoSimplesDTO;
import com.autobots.automanager.dtos.usuario.UsuarioSimplesDTO;
import com.autobots.automanager.dtos.venda.VendaCompletaDTO;
import com.autobots.automanager.entidades.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class EmpresaCompletaDTO extends RepresentationModel<EmpresaCompletaDTO> {
    private Long id;
    private String razaoSocial;
    private String nomeFantasia;
    private Date cadastro;
    private Set<Telefone> telefones = new HashSet<>();
    private Endereco endereco;
    private List<UsuarioSimplesDTO> usuarios;
    private List<MercadoriaCompletaDTO> mercadorias;
    private List<ServicoSimplesDTO> servicos;
    private List<VendaCompletaDTO> vendas;

    public static EmpresaCompletaDTO from(Empresa empresa){
        EmpresaCompletaDTO dto = new EmpresaCompletaDTO();
        dto.setId(empresa.getId());
        dto.setRazaoSocial(empresa.getRazaoSocial());
        dto.setNomeFantasia(empresa.getNomeFantasia());
        dto.setCadastro(empresa.getCadastro());
        dto.setTelefones(empresa.getTelefones());
        dto.setEndereco(empresa.getEndereco());
        dto.setUsuarios(UsuarioSimplesDTO.from(empresa.getUsuarios()));
        dto.setMercadorias(MercadoriaCompletaDTO.from(empresa.getMercadorias()));
        dto.setServicos(ServicoSimplesDTO.from(empresa.getServicos().stream().toList()));
        dto.setVendas(VendaCompletaDTO.from(empresa.getVendas().stream().toList()));
        return dto;
    }

    public static List<EmpresaCompletaDTO> from(List<Empresa> empresas){
        return empresas.stream().map(EmpresaCompletaDTO::from).collect(Collectors.toList());
    }

}
