package com.autobots.automanager.entidades;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.autobots.automanager.enumeracoes.TipoDocumento;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Documento extends RepresentationModel<Documento> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private TipoDocumento tipo;
	@Column(nullable = false)
	private Date dataEmissao;
	@Column(unique = true, nullable = false)
	private String numero;
}