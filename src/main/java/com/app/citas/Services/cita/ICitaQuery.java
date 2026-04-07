package com.app.citas.Services.cita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.app.citas.Entity.Cita;
import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Mapper.Model.CitalModel;

public interface ICitaQuery {

    public List<CitalModel> ObtenerCitasPendientes(Long idCliente, Long idNegocio);

    public LocalDate buscarSiguienteDiaDisponible(SesionWhatsapp sesion);

    public List<LocalTime> procesarHoraUsuario(SesionWhatsapp sesion);

    public List<LocalTime> obtenerHorariosDisponibles(SesionWhatsapp sesion);

    public List<Cita> verficarCantidadCitasAgendadas(String telefono, LocalDate fecha);

}
