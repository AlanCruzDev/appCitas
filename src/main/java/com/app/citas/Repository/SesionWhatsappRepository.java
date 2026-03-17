package com.app.citas.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.citas.Entity.SesionWhatsapp;

public interface SesionWhatsappRepository extends JpaRepository<SesionWhatsapp, Long>{
    
    Optional<SesionWhatsapp> findByTelefono(String telefono);    
}
