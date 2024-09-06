package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.entidades.Mercadoria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MercadoriaAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Mercadoria mercadoria, Mercadoria atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(String.valueOf(atualizacao.getNome()))) {
				mercadoria.setNome(atualizacao.getNome());
			}
			if (!verificador.verificar(atualizacao.getDescricao())) {
				mercadoria.setDescricao(atualizacao.getDescricao());
			}
			if (mercadoria.getCadastro() != null) {
					mercadoria.setCadastro(atualizacao.getCadastro());
			}
			if (mercadoria.getValidade() != null) {
				mercadoria.setValidade(atualizacao.getValidade());
			}
			if (mercadoria.getFabricao() != null){
				mercadoria.setFabricao(atualizacao.getFabricao());
			}
		}
	}

	public void atualizar(List<Mercadoria> mercadorias, List<Mercadoria> atualizacoes) {
		for (Mercadoria atualizacao : atualizacoes) {
			for (Mercadoria mercadoria : mercadorias) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == mercadoria.getId()) {
						atualizar(mercadoria, atualizacao);
					}
				}
			}
		}
	}
}
