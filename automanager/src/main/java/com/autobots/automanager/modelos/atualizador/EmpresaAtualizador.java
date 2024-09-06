package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.entidades.Empresa;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmpresaAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Empresa empresa, Empresa atualizacao) {
		if (atualizacao != null) {
			if (!verificador.verificar(String.valueOf(atualizacao.getNomeFantasia()))) {
				empresa.setNomeFantasia(atualizacao.getNomeFantasia());
			}
			if (!verificador.verificar(atualizacao.getRazaoSocial())) {
				empresa.setRazaoSocial(atualizacao.getRazaoSocial());
			}
		}
	}

	public void atualizar(List<Empresa> empresas, List<Empresa> atualizacoes) {
		for (Empresa atualizacao : atualizacoes) {
			for (Empresa empresa : empresas) {
				if (atualizacao.getId() != null) {
					if (atualizacao.getId() == empresa.getId()) {
						atualizar(empresa, atualizacao);
					}
				}
			}
		}
	}
}
