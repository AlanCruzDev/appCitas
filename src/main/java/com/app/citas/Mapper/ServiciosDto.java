package com.app.citas.Mapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiciosDto {

    private Long idServicio;
    private String nombre;
    private int duracion_minutos;
    private float precio;
    private boolean activo;
    private Long idNegocio;
    
    
}
