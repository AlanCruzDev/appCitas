package com.app.citas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.citas.Entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente,Long>{
    
}
