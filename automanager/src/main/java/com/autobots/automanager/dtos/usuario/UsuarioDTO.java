package com.autobots.automanager.dtos.usuario;

import com.autobots.automanager.dtos.veiculo.VeiculoDTO;
import com.autobots.automanager.dtos.venda.VendaDTO;
import com.autobots.automanager.entidades.*;
import com.autobots.automanager.enumeracoes.PerfilUsuario;

import java.util.Set;

public class UsuarioDTO {
    private Long id;
    private String nome;
    private String nomeSocial;
    private Set<PerfilUsuario> perfis;
    private Set<Telefone> telefones;
    private Endereco endereco;
    private Set<Documento> documentos;
    private Set<Email> emails;
    private Set<Credencial> credenciais;
    private Set<Mercadoria> mercadorias;
    private Set<VendaDTO> vendas;
    private Set<VeiculoDTO> veiculos;


}
