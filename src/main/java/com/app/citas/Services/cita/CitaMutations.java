package com.app.citas.Services.cita;

import java.time.LocalTime;

import com.app.citas.Estados.SesionUsuario;

public interface CitaMutations {
    public String guardarCita(SesionUsuario serviciosModel,LocalTime horaSelect);
    
}
