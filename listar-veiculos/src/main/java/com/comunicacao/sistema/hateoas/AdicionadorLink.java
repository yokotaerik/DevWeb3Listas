package com.comunicacao.sistema.hateoas;

import java.util.List;

public interface AdicionadorLink<T> {
    public void adicionarLink(List<T> lista, String token, Long empresaId);
    public void adicionarLink(T objeto, Long empresaId, String token);
}