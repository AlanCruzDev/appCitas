package com.app.citas.Mapper;

import java.time.LocalDate;

import com.app.citas.Entity.Roles;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDto {

    private String nombre;
    private String email;
    private String password;
    private Roles roles;
    private boolean activo;
    private LocalDate fechaCreacion;
    private boolean recibeCitas;
    private Long idnegocio;
    private String telefono;

}
