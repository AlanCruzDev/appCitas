package com.app.citas.Mapper;

import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NegocioDto {
    
    private String nombre;
    private String direccion;
    private String telefono;
    private LocalTime hora_apertura;
    private LocalTime hora_cierre;
    private boolean activo;
    private Long idUsuario;
    
}
