package com.comunicacao.sistema.dtos.responseDTO;

import com.comunicacao.sistema.dtos.empresa.EmpresaSimplesDTO;
import com.comunicacao.sistema.dtos.mercadoria.MercadoriaSimplesDTO;
import com.comunicacao.sistema.dtos.usuario.UsuarioDTO;
import com.comunicacao.sistema.dtos.veiculo.VeiculoDTO;
import com.comunicacao.sistema.dtos.venda.VendaCompletaDTO;
import com.comunicacao.sistema.entidades.*;
import com.comunicacao.sistema.enumeracoes.Perfil;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Set;

@Data
public class UsuarioResponseDTO extends RepresentationModel<UsuarioResponseDTO> {
    private Long id;
    private String nome;
    private String nomeSocial;
    private Set<Perfil> perfis;
    private Set<Telefone> telefones;
    private Endereco endereco;
    private Set<Documento> documentos;
    private Set<Email> emails;
    private EmpresaSimplesDTO empresa;
    private List<MercadoriaSimplesDTO> mercadorias;
    private List<VendaCompletaDTO> vendas;
    private List<VeiculoDTO> veiculos;


    public static UsuarioResponseDTO from(UsuarioDTO dto){
        UsuarioResponseDTO usuarioResponseDTO = new UsuarioResponseDTO();
        usuarioResponseDTO.setId(dto.getId());
        usuarioResponseDTO.setNome(dto.getNome());
        usuarioResponseDTO.setNomeSocial(dto.getNomeSocial());
        usuarioResponseDTO.setPerfis(dto.getPerfis());
        usuarioResponseDTO.setTelefones(dto.getTelefones());
        usuarioResponseDTO.setEndereco(dto.getEndereco());
        usuarioResponseDTO.setDocumentos(dto.getDocumentos());
        usuarioResponseDTO.setEmails(dto.getEmails());
        usuarioResponseDTO.setEmpresa(dto.getEmpresa());
        usuarioResponseDTO.setMercadorias(dto.getMercadorias());
        usuarioResponseDTO.setVendas(dto.getVendas());
        usuarioResponseDTO.setVeiculos(dto.getVeiculos());
        return usuarioResponseDTO;
    }

    public static List<UsuarioResponseDTO> from(List<UsuarioDTO> dtos){
        return dtos.stream().map(UsuarioResponseDTO::from).toList();
    }
}
