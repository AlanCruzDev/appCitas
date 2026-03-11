package com.app.citas.Services.clientes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.Cliente;
import com.app.citas.Repository.ClienteRepository;

@Service
@Transactional(readOnly = true)
public class ClienteQueryImpl implements ClienteQuery{

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente obtenerClienteByNumero(String telefono) {
        return this.clienteRepository.obtenerClienteByNumero(telefono); 
    }
    
}
