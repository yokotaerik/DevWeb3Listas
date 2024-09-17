package com.autobots.automanager.entidades;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode
@Data
@Entity
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
}