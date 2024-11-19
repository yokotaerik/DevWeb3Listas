package com.autobots.automanager.entidades;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.autobots.automanager.modelos.Perfil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

@Data
@EqualsAndHashCode(of = { "id" })
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Usuario extends RepresentationModel<Usuario> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String nome;
	@Column
	private String nomeSocial;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Perfil> perfis = new HashSet<>();

	@JsonIgnore()
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Telefone> telefones = new HashSet<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Endereco endereco;

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Documento> documentos = new HashSet<>();

	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Email> emails = new HashSet<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Credencial credencial;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	private Set<Mercadoria> mercadorias = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JsonIgnore
	private Set<Venda> vendas = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JsonIgnore
	private Set<Veiculo> veiculos = new HashSet<>();

	@ManyToOne
	private Empresa empresa;

	public Set<Perfil> getPerfis() {
		if(perfis.contains(Perfil.ROLE_ADMIN)){
			perfis.add(Perfil.ROLE_CLIENTE);
			perfis.add(Perfil.ROLE_GERENTE);
			perfis.add(Perfil.ROLE_VENDEDOR);
			return perfis;
		}
		if(perfis.contains(Perfil.ROLE_GERENTE)){
			perfis.add(Perfil.ROLE_VENDEDOR);
			perfis.add(Perfil.ROLE_CLIENTE);
			return perfis;
		}
		if(perfis.contains(Perfil.ROLE_VENDEDOR)){
			perfis.add(Perfil.ROLE_CLIENTE);
			return perfis;
		}
		return perfis;
	};
}