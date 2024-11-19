package com.autobots.automanager.entidades;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@Entity
@EqualsAndHashCode(of = "id")
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

	@JsonIgnore
	@ManyToOne()
	private Empresa empresa;

	@JsonIgnore
	@ManyToMany(mappedBy = "mercadorias")
	private List<Venda> vendas = new ArrayList<Venda>();
}