package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.entidades.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsuarioAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Usuario usuario, Usuario atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(String.valueOf(atualizacao.getNome()))) {
				usuario.setNome(atualizacao.getNome());
			}
			if (!verificador.verificar(atualizacao.getNomeSocial())) {
				usuario.setNomeSocial(atualizacao.getNomeSocial());
			}
			if (atualizacao.getCredencial() != null && !atualizacao.getCredencial().getNomeUsuario().isEmpty() && !atualizacao.getCredencial().getSenha().isEmpty()) {
				usuario.getCredencial().setNomeUsuario(atualizacao.getCredencial().getNomeUsuario());
				BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
				usuario.getCredencial().setSenha(codificador.encode(atualizacao.getCredencial().getSenha()));
			}
			if (!atualizacao.getPerfis().isEmpty()){
				usuario.getPerfis().clear();
				usuario.getPerfis().addAll(atualizacao.getPerfis());
			}
		}
	}

	public void atualizar(List<Usuario> usuarios, List<Usuario> atualizacoes) {
		for (Usuario atualizacao : atualizacoes) {
			for (Usuario usuario : usuarios) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == usuario.getId()) {
						atualizar(usuario, atualizacao);
					}
				}
			}
		}
	}
}
