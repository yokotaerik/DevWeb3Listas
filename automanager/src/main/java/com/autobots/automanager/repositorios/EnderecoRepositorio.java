package com.autobots.automanager.repositorios;

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepositorio extends JpaRepository<Endereco, Long> {
}
