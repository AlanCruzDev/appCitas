package com.app.citas.Services.clientes;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.Cliente;
import com.app.citas.Repository.ClienteRepository;

@Service
@Transactional
public class ClienteMutationsImpl implements ClienteMutations {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente guardarCliente(String telefono) {

        Cliente cliente = new Cliente();
        cliente.setNombre("S/N");
        cliente.setTelefono(telefono);
        cliente.setFechaRegistro(LocalDateTime.now());
        cliente.setUsuario(null);
        return this.clienteRepository.save(cliente);
    }

    @Override
    public Cliente guardarOrActualizar(Cliente cliente) {
        return this.clienteRepository.save(cliente);
    }
}
