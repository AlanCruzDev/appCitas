package com.app.citas.Services.cita;

import java.time.LocalTime;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Mapper.body.CitaBody;

public interface CitaMutations {

    public String guardarCitaRapida(CitaBody citaBody);

    public String guardarCitaSesion(SesionWhatsapp sesion);

    public String guardarCita(SesionWhatsapp sesion, LocalTime horaSelect);

}
