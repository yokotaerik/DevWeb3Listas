package com.autobots.automanager.entidades;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Entity
public class Mercadoria extends RepresentationModel<Mercadoria> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Date validade;

	@Column(nullable = false)
	private Date fabricao;

	@Column(nullable = false)
	private Date cadastro;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false)
	private long quantidade;

	@Column(nullable = false)
	private double valor;

	@Column
	private String descricao;
}