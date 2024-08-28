package com.autobots.automanager.repositorios;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelefoneRepostorio extends JpaRepository<Telefone, Long> {
}
