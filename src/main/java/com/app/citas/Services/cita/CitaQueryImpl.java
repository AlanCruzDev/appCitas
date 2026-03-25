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
import com.app.citas.Services.clientes.ClienteQuery;
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

    @Autowired
    private ClienteQuery clienteQuery;

    @Override
    public List<Cita> verficarCantidadCitasAgendadas(String telefono, LocalDate fecha) {
        Long clienteId = this.clienteQuery.obtenerClienteByNumero(telefono).getId();
        List<Cita> cantidad = this.citaRepository.obtenerCitasDelDia(clienteId, fecha);
        return cantidad;
    }

    @Override
    public LocalDate buscarSiguienteDiaDisponible(SesionWhatsapp sesion) {
        LocalDate fecha = sesion.getFechaCreacion();
        for (int i = 1; i <= 7; i++) {
            LocalDate nuevaFecha = fecha.plusDays(i);
            sesion.setFechaCreacion(nuevaFecha);
            List<LocalTime> horarios = procesarHoraUsuario(sesion);

            if (!horarios.isEmpty()) {
                return nuevaFecha;
            }

        }
        return null;
    }

    @Override
    public List<LocalTime> procesarHoraUsuario(SesionWhatsapp sesion) {

        List<LocalTime> disponibles = obtenerHorariosDisponibles(sesion);
        return disponibles;
    }

    @Override
    public List<LocalTime> obtenerHorariosDisponibles(SesionWhatsapp sesion) {

        List<Cita> citas = citaRepository.obtenerCitasDelDia(sesion.getSucursalId(), sesion.getFechaCreacion(),
                sesion.getEmpleadoId());
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

    public String formatearHora(LocalTime hora) {
        return hora.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
    }

}
