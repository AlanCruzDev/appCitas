package com.app.citas.Services.clientes;

import org.springframework.data.repository.query.Param;

import com.app.citas.Entity.Cliente;

public interface ClienteQuery {
    public Cliente obtenerClienteByNumero(@Param("telefono") String telefono); 
}
