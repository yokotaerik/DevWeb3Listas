package com.autobots.automanager.service;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UsuarioRepositorio usuarioRepositorio;

    public String obterNomeUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                return userDetails.getUsername(); // Retorna o nome do usuário logado
            }
            return authentication.getName(); // Caso seja uma String ou outro tipo
        }
        return null; // Usuário não está logado
    }

    public Usuario obterUsuarioLogado() {
        var nomeUsuario = obterNomeUsuarioLogado();

        if(nomeUsuario != null) {
            var usuarioOp =  usuarioRepositorio.findyByCredencialNomeUsuario(nomeUsuario);
            if (usuarioOp.isPresent()) {
                return usuarioOp.get();
            } else {
                return null;
            }
        }

        return null;
    }
}
