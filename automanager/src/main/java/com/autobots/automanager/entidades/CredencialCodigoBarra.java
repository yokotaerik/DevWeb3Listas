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
public class CredencialCodigoBarra extends Credencial {
	@Column(nullable = false, unique = true)
	private long codigo;

	public CredencialCodigoBarra(Long codigo) {
		this.setCriacao(new Date());
		this.setInativo(false);
		this.setCodigo(codigo);
		this.setTipo(TipoCredencial.CODIGO_BARRAS);
	}

	public CredencialCodigoBarra() {
		super();
	}
}