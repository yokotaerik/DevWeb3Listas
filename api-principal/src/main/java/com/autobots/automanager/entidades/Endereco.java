package com.autobots.automanager.entidades;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Endereco extends RepresentationModel<Endereco> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String estado;

	@Column(nullable = false)
	private String cidade;

	@Column(nullable = false)
	private String bairro;

	@Column(nullable = false)
	private String rua;

	@Column(nullable = false)
	private String numero;

	@Column(nullable = false)
	private String codigoPostal;

	@Column()
	private String informacoesAdicionais;


	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

	@JsonIgnore()
	@OneToOne
	@JoinColumn(name = "empresa_id")
	private Empresa empresa;
}