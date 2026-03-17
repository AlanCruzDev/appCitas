package com.app.citas.Estados;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SesionUsuario {
    int selectMenu;
    Long sucursalId;
    Long servicioId;
    Long clienteId;
    Long empleadoId;
    LocalDate fecha;
    LocalTime hora;
    String telefono;
}
