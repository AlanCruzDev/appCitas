package com.app.citas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.citas.Entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Long>{


    @Query("SELECT u FROM Usuario u where u.activo =true and u.rol ='DUENO' and u.id=:id")
    public Usuario encontrarDuenosById(@Param("id") Long id);
    
}
