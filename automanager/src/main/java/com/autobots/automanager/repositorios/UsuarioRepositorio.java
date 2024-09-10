package com.autobots.automanager.repositorios;

import com.autobots.automanager.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {
}
