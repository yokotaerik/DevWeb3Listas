package com.comunicacao.sistema.dtos.usuario;

import com.comunicacao.sistema.dtos.empresa.EmpresaSimplesDTO;
import com.comunicacao.sistema.dtos.mercadoria.MercadoriaSimplesDTO;
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

}
