package com.app.citas.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.citas.Entity.Servicio;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {

    @Query("SELECT s FROM Servicio s JOIN s.usuarios u WHERE s.negocio.idNegocio = :idNegocio AND u.id = :idUsuario")
    List<Servicio> obtenerServiciosByNegocioYUsuario(@Param("idNegocio") Long idNegocio,
            @Param("idUsuario") Long idUsuario);

}
