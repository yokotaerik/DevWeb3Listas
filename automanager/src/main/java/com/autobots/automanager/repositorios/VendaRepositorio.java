package com.autobots.automanager.repositorios;

import com.autobots.automanager.entidades.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendaRepositorio extends JpaRepository<Venda, Long> {
}
