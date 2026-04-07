package com.app.citas.Mapper;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiciosDto {

    private Long idServicio;
    private String nomServicio;
    private int duracionMin;
    private String infoServicio;
    private float precio;
    private boolean activo;
    private Long idNegocio;
    private Long idUsuario;
}
