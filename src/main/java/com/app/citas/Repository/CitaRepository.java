package com.app.citas.Repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.app.citas.Entity.Cita;

public interface CitaRepository extends JpaRepository<Cita, Long> {

        @Query("SELECT c FROM Cita c where c.negocio.idNegocio=:negocioId and c.fecha =:fecha and c.estado = 'AGENDADA' and c.empleado.id =:idEmpleado")
        List<Cita> obtenerCitasDelDia(@Param("negocioId") Long negocioId, @Param("fecha") LocalDate fecha,
                        @Param("idEmpleado") long idEmpleado);
}
