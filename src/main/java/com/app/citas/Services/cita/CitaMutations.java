package com.app.citas.Services.cita;

import java.time.LocalTime;

import com.app.citas.Entity.SesionWhatsapp;
public interface CitaMutations {
    public String guardarCita(SesionWhatsapp sesion,LocalTime horaSelect);
    
}
