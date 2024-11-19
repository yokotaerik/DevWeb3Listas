package com.autobots.automanager.dtos.empresa;

import com.autobots.automanager.entidades.Empresa;
import lombok.Data;

@Data
public class EmpresaSimplesDTO {
    private Long id;
    private String razaoSocial;
    private String nomeFantasia;

    public static EmpresaSimplesDTO from(Empresa empresa){
        EmpresaSimplesDTO dto = new EmpresaSimplesDTO();
        dto.setId(empresa.getId());
        dto.setRazaoSocial(empresa.getRazaoSocial());
        dto.setNomeFantasia(empresa.getNomeFantasia());
        return dto;
    }
}
