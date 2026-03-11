package com.app.citas.Mapper.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiciosModel {
    
    private Long idServicio;
    private String nombre;
    private int duracion_minutos;
    private float precio;
    private boolean activo;
    private String telefono;
    
}
