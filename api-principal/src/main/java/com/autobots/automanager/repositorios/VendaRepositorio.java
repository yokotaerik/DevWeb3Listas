package com.autobots.automanager.repositorios;

import com.autobots.automanager.dtos.venda.VendaCompletaDTO;
import com.autobots.automanager.entidades.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VendaRepositorio extends JpaRepository<Venda, Long> {
    @Query("SELECT new com.autobots.automanager.dtos.venda.VendaCompletaDTO(v.id, v.cadastro, v.identificacao, v.cliente, v.funcionario, v.veiculo, v.mercadorias, v.servicos) FROM Venda v")
    List<VendaCompletaDTO> findAllVendasAsDTO();
}