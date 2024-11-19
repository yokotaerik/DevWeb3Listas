package com.comunicacao.sistema.entidades;

import com.comunicacao.sistema.enumeracoes.TipoVeiculo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(exclude = { "proprietario", "vendas" })
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Veiculo extends RepresentationModel<Veiculo> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private TipoVeiculo tipo;

	@Column(nullable = false)
	private String modelo;

	@Column(nullable = false)
	private String placa;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	private Usuario proprietario;

	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JsonIgnore
	private Set<Venda> vendas = new HashSet<>();
}