package com.app.citas.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.citas.Entity.Cita;

public interface CitaRepository extends JpaRepository<Cita,Long>{


    @Query("SELECT c FROM Cita c where c.negocio.idNegocio=:negocioId and c.fecha =:fecha and c.estado = 'AGENDADA' ")
    List<Cita> obtenerCitasDelDia(Long negocioId, LocalDate fecha);
}
