package com.comunicacao.sistema.entidades;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Data
@Entity
public class Credencial implements Serializable{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String nomeUsuario;
	
	@Column(nullable = false)
	private String senha;
}