package com.app.citas.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.citas.Entity.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio,Long>{

    
    @Query("SELECT s FROM Servicio s where s.negocio.idNegocio =:id")
    public List<Servicio> ObtenerServiciosByNegocio(@Param("id") Long id);
    
}
