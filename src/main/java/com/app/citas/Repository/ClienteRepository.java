package com.app.citas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.citas.Entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente,Long>{
    

    @Query("SELECT c FROM Cliente c WHERE c.telefono =:telefono")
    public Cliente obtenerClienteByNumero(@Param("telefono") String telefono);
}
