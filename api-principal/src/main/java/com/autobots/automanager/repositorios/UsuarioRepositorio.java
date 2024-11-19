package com.autobots.automanager.repositorios;

import com.autobots.automanager.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
    @Query("SELECT u FROM Usuario u WHERE u.credencial.nomeUsuario = ?1")
    Optional<Usuario> findyByCredencialNomeUsuario(String nomeUsuario);
}
