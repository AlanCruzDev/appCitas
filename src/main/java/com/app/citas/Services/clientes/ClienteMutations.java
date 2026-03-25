package com.app.citas.Services.clientes;

import com.app.citas.Entity.Cliente;

public interface ClienteMutations {

    public Cliente guardarCliente(String telefono);

    public Cliente guardarOrActualizar(Cliente cliente);

}
