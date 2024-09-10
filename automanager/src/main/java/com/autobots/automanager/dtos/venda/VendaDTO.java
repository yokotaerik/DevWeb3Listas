package com.autobots.automanager.dtos.venda;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

public class VendaDTO {
    private Long id;
    private Date cadastro;
    private String identificacao;
}
