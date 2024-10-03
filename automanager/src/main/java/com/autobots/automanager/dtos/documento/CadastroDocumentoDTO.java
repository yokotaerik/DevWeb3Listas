package com.autobots.automanager.dtos.documento;

import com.autobots.automanager.enumeracoes.TipoDocumento;
import lombok.Data;
import java.util.Date;

@Data
public class CadastroDocumentoDTO {

    private TipoDocumento tipo;
    private Date dataEmissao;
    private String numero;
    private Long clienteId;

}
