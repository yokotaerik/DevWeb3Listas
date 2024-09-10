package com.autobots.automanager.modelos.atualizador;

import java.util.List;

import com.autobots.automanager.entidades.CredencialCodigoBarra;
import org.springframework.stereotype.Component;

@Component
public class CredencialCodigoBarraAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(CredencialCodigoBarra credencial, CredencialCodigoBarra atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(String.valueOf(atualizacao.getCodigo()))) {
                credencial.setCodigo(atualizacao.getCodigo());
            }
        }
    }

    public void atualizar(List<CredencialCodigoBarra> credencials, List<CredencialCodigoBarra> atualizacoes) {
        for (CredencialCodigoBarra atualizacao : atualizacoes) {
            for (CredencialCodigoBarra credencial : credencials) {
                if (atualizacao.getId() != null) {
                    if (atualizacao.getId() == credencial.getId()) {
                        atualizar(credencial, atualizacao);
                    }
                }
            }
        }
    }
}
