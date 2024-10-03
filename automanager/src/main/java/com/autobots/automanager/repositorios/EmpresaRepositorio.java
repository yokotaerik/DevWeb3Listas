package com.autobots.automanager.repositorios;

import com.autobots.automanager.entidades.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpresaRepositorio extends JpaRepository<Empresa, Long> {
}
