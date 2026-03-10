package com.app.citas.Mapper.Model;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NegocioModel {

    private Long idNegocio;
    private String nombre;
    private String direccion;
    private String telefono;
    private LocalTime hora_apertura;
    private LocalTime hora_cierre;
    private boolean activo;
    
}
