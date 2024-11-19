package com.autobots.automanager.dtos.emails;

public class CadastroEmailDTO {
    private String email;
    private long clienteId;

    public CadastroEmailDTO(String email, long clienteId) {
        this.email = email;
        this.clienteId = clienteId;
    }

    public String getEmail() {
        return email;
    }

    public long getClienteId() {
        return clienteId;
    }
}
