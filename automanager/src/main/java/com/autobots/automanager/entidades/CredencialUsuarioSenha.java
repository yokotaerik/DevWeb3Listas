package com.autobots.automanager.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.autobots.automanager.enumeracoes.TipoCredencial;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CredencialUsuarioSenha extends Credencial {
	@Column(nullable = false, unique = true)
	private String nomeUsuario;
	@Column(nullable = false)
	private String senha;

	public CredencialUsuarioSenha(String nomeUsuario, String senha) {
		this.setCriacao(new Date());
		this.setInativo(false);
		this.setNomeUsuario(nomeUsuario);
		this.setSenha(senha);
		this.setTipo(TipoCredencial.USUARIO_SENHA);
	}


	public CredencialUsuarioSenha() {
		super();
	}
}