package com.autobots.automanager.entidades;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Servico extends RepresentationModel<Servico> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String nome;
	@Column(nullable = false)
	private double valor;
	@Column
	private String descricao;

	@JsonIgnore
	@ManyToOne()
	private Empresa empresa;

	@JsonIgnore
	@ManyToMany(mappedBy = "servicos")
	private List<Venda> vendas = new ArrayList<Venda>();
}