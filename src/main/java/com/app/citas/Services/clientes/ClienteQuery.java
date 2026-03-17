package com.app.citas.Services.clientes;
import com.app.citas.Entity.Cliente;

public interface ClienteQuery {
    public Cliente obtenerClienteByNumero(String telefono); 
    public Cliente obtenerClienteById(Long id);
}
