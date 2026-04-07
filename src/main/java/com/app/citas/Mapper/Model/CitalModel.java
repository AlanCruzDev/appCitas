package com.app.citas.Mapper.Model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.app.citas.Entity.EstadoCita;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CitalModel {

    private Long id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoCita estado;
    private ClienteModel cliente;
    private ServiciosModel servicio;

}
