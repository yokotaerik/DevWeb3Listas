package com.comunicacao.sistema.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Telefone extends RepresentationModel<Telefone> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String ddd;
	@Column(nullable = false)
	private String numero;

	@ManyToOne
	@JoinColumn(name = "usuario_id")
	private Usuario usuario;

}