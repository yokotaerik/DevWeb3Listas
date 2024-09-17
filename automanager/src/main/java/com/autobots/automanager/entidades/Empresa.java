package com.autobots.automanager.entidades;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Empresa extends RepresentationModel<Empresa> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String razaoSocial;

	@Column
	private String nomeFantasia;

	@Column(nullable = false)
	private Date cadastro;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Telefone> telefones = new HashSet<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Endereco endereco;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Usuario> usuarios = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Mercadoria> mercadorias = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Servico> servicos = new HashSet<>();

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private Set<Venda> vendas = new HashSet<>();

	public Empresa(String nomeFantasia, String razaoSocial) {
		this.nomeFantasia = nomeFantasia;
		this.razaoSocial = razaoSocial;
		this.cadastro = new Date();
	}

	public Empresa() {}
}