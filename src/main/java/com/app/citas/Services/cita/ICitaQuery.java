package com.app.citas.Services.cita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.app.citas.Entity.Cita;
import com.app.citas.Entity.SesionWhatsapp;

public interface ICitaQuery {

    public LocalDate buscarSiguienteDiaDisponible(SesionWhatsapp sesion);

    public List<LocalTime> procesarHoraUsuario(SesionWhatsapp sesion);

    public List<LocalTime> obtenerHorariosDisponibles(SesionWhatsapp sesion);

    public List<Cita> verficarCantidadCitasAgendadas(String telefono, LocalDate fecha);

}
