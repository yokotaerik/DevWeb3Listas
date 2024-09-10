package com.autobots.automanager.modelos.atualizador;

import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CredencialUsuarioSenhaAtualizador {
    private StringVerificadorNulo verificador = new StringVerificadorNulo();

    public void atualizar(CredencialUsuarioSenha credencial, CredencialUsuarioSenha atualizacao) {
        if (atualizacao != null) {
            if (!verificador.verificar(String.valueOf(atualizacao.getNomeUsuario()))) {
                credencial.setNomeUsuario(atualizacao.getNomeUsuario());
            }
            if (!verificador.verificar(String.valueOf(atualizacao.getSenha()))) {
                credencial.setSenha(atualizacao.getSenha());
            }
        }
    }

    public void atualizar(List<CredencialUsuarioSenha> credencials, List<CredencialUsuarioSenha> atualizacoes) {
        for (CredencialUsuarioSenha atualizacao : atualizacoes) {
            for (CredencialUsuarioSenha credencial : credencials) {
                if (atualizacao.getId() != null) {
                    if (atualizacao.getId() == credencial.getId()) {
                        atualizar(credencial, atualizacao);
                    }
                }
            }
        }
    }
}
