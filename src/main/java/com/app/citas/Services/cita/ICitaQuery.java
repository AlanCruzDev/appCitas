package com.app.citas.Services.cita;

import java.time.LocalTime;
import java.util.List;

import com.app.citas.Entity.SesionWhatsapp;

public interface ICitaQuery {

    public List<LocalTime> procesarHoraUsuario(SesionWhatsapp sesion);

    public List<LocalTime> obtenerHorariosDisponibles(SesionWhatsapp sesion);
}
