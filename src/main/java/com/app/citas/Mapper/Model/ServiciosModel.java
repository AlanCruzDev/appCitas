package com.app.citas.Mapper.Model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServiciosModel {

    private Long idServicio;
    private float precio;
    private String nomServicio;
    private int duracionMin;
    private String infoServicio;
    private boolean activo;
    private List<UsuarioModel> usuarioModels;

}
