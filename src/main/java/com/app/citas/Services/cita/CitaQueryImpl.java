package com.app.citas.Services.cita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.citas.Entity.Cita;
import com.app.citas.Entity.Negocio;
import com.app.citas.Entity.Servicio;
import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Repository.CitaRepository;
import com.app.citas.Services.negocio.INegocioQuery;
import com.app.citas.Services.servicios.IServicioQuery;

@Service
public class CitaQueryImpl implements ICitaQuery {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private IServicioQuery iServicioQuery;

    @Autowired
    private INegocioQuery negocioQuery;

    @Override
    public boolean cupoDisponible(SesionWhatsapp sesionWhatsapp) {

        List<Cita> citas = citaRepository.obtenerCitasDelDia(sesionWhatsapp.getSucursalId(),
                sesionWhatsapp.getFechaCreacion());
        Servicio servicio = this.iServicioQuery.findByServicio(sesionWhatsapp.getServicioId());
        Negocio negocio = this.negocioQuery.encontrarNegocioById(sesionWhatsapp.getSucursalId());

        List<LocalTime> horarios = generarHorariosDisponibles(
                negocio.getHora_apertura(),
                negocio.getHora_cierre(),
                servicio.getDuracionMinutos(),
                citas);
        boolean ocupado = horarios.stream().anyMatch(h -> h.equals(sesionWhatsapp.getHora()));
        return ocupado;
    }

    // OBTENER CITAS DEL DIA EN ACCION DEL MENU

    @Override
    public List<LocalTime> obtenerHorariosDisponibles(SesionWhatsapp sesion) {

        List<Cita> citas = citaRepository.obtenerCitasDelDia(sesion.getSucursalId(), LocalDate.now());
        Servicio servicio = this.iServicioQuery.findByServicio(sesion.getServicioId());
        Negocio negocio = this.negocioQuery.encontrarNegocioById(sesion.getSucursalId());

        List<LocalTime> horarios = generarHorariosDisponibles(
                negocio.getHora_apertura(),
                negocio.getHora_cierre(),
                servicio.getDuracionMinutos(),
                citas);
        return horarios;

    }

    public List<LocalTime> generarHorariosDisponibles(
            LocalTime apertura,
            LocalTime cierre,
            int duracionServicio,
            List<Cita> citasDelDia) {

        List<LocalTime> horariosDisponibles = new ArrayList<>();

        LocalTime horaActual = apertura;

        while (horaActual.plusMinutes(duracionServicio).isBefore(cierre)
                || horaActual.plusMinutes(duracionServicio).equals(cierre)) {

            LocalTime horaFin = horaActual.plusMinutes(duracionServicio);
            boolean ocupado = false;
            for (Cita cita : citasDelDia) {
                if (!(horaFin.isBefore(cita.getHoraInicio()) ||
                        horaActual.isAfter(cita.getHoraFin()))) {
                    ocupado = true;
                    break;
                }
            }
            if (!ocupado) {
                horariosDisponibles.add(horaActual);
            }
            horaActual = horaActual.plusMinutes(duracionServicio);
        }
        return horariosDisponibles;
    }

}
