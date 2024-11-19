package com.comunicacao.sistema.dtos.usuario;

import com.comunicacao.sistema.dtos.empresa.EmpresaSimplesDTO;
import com.comunicacao.sistema.dtos.mercadoria.MercadoriaSimplesDTO;
import com.comunicacao.sistema.dtos.veiculo.VeiculoDTO;
import com.comunicacao.sistema.dtos.venda.VendaCompletaDTO;
import com.comunicacao.sistema.entidades.*;
import com.comunicacao.sistema.enumeracoes.Perfil;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Set;

@Data
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> {
    private Long id;
    private String nome;
    private String nomeSocial;
    private Set<Perfil> perfis;
    private Set<Telefone> telefones;
    private Endereco endereco;
    private Set<Documento> documentos;
    private Set<Email> emails;
    private Credencial credencial;
    private EmpresaSimplesDTO empresa;
    private List<MercadoriaSimplesDTO> mercadorias;
    private List<VendaCompletaDTO> vendas;
    private List<VeiculoDTO> veiculos;

    public static UsuarioDTO from(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setNomeSocial(usuario.getNomeSocial());
        dto.setPerfis(usuario.getPerfis());
        dto.setTelefones(usuario.getTelefones());
        dto.setEndereco(usuario.getEndereco());
        dto.setDocumentos(usuario.getDocumentos());
        dto.setEmails(usuario.getEmails());
        dto.setCredencial(usuario.getCredencial());
        dto.setMercadorias(MercadoriaSimplesDTO.from(usuario.getMercadorias().stream().toList()));
        dto.setVendas(VendaCompletaDTO.from(usuario.getVendas().stream().toList()));
        dto.setVeiculos(VeiculoDTO.from(usuario.getVeiculos().stream().toList()));
        return dto;
    }

    public static List<UsuarioDTO> from(List<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioDTO::from).toList();
    }
}
