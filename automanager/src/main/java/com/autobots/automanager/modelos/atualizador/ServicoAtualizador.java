package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Servico;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ServicoAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Servico servico, Servico atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(String.valueOf(atualizacao.getNome()))) {
				servico.setNome(atualizacao.getNome());
			}
			if (!verificador.verificar(atualizacao.getDescricao())) {
				servico.setDescricao(atualizacao.getDescricao());
			}
		}
	}

	public void atualizar(List<Servico> servicos, List<Servico> atualizacoes) {
		for (Servico atualizacao : atualizacoes) {
			for (Servico servico : servicos) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == servico.getId()) {
						atualizar(servico, atualizacao);
					}
				}
			}
		}
	}
}
