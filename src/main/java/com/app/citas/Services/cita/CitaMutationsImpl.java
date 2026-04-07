package com.app.citas.Services.cita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.Cita;
import com.app.citas.Entity.Cliente;
import com.app.citas.Entity.EstadoCita;
import com.app.citas.Entity.Negocio;
import com.app.citas.Entity.Servicio;
import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Entity.TipoCita;
import com.app.citas.Entity.Usuario;
import com.app.citas.Mapper.body.CitaBody;
import com.app.citas.Repository.CitaRepository;
import com.app.citas.Services.clientes.ClienteQuery;
import com.app.citas.Services.negocio.INegocioQuery;
import com.app.citas.Services.servicios.IServicioQuery;
import com.app.citas.Services.usuario.EmpleadoQuery;

@Service
@Transactional
public class CitaMutationsImpl implements CitaMutations {

    @Autowired
    private ICitaQuery citaQuery;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private IServicioQuery iServicioQuery;

    @Autowired
    private INegocioQuery negocioQuery;

    @Autowired
    private ClienteQuery clienteQuery;

    @Autowired
    private EmpleadoQuery empleadoQuery;

    @Override
    public String guardarCitaRapida(CitaBody citaBody) {

        Servicio ser = this.iServicioQuery.findByServicio(citaBody.getIdSerivio());

        SesionWhatsapp whatsapp = new SesionWhatsapp();
        whatsapp.setSucursalId(citaBody.getIdNegocio());
        whatsapp.setEmpleadoId(citaBody.getIdUsuario());
        whatsapp.setFechaCreacion(LocalDate.now());
        whatsapp.setServicioId(citaBody.getIdSerivio());

        List<LocalTime> disponibles = this.citaQuery.procesarHoraUsuario(whatsapp);

        if (disponibles.isEmpty()) {
            return "❌ No hay horarios disponibles en este momento";
        }

        LocalTime ahora = LocalTime.now();
        List<LocalTime> disponiblesFiltrados = disponibles.stream()
                .filter(h -> h.isAfter(ahora))
                .toList();

        if (disponiblesFiltrados.isEmpty()) {
            return "⏳ Ya no hay horarios disponibles para hoy";
        }

        LocalTime horaInicio = disponiblesFiltrados.get(0);
        LocalTime horaFin = horaInicio.plusMinutes(ser.getDuracionMinutos());

        Cita cita = new Cita();
        cita.setFecha(LocalDate.now());
        cita.setHoraInicio(horaInicio);
        cita.setHoraFin(horaFin);
        cita.setServicio(ser);

        Negocio negocio = new Negocio();
        negocio.setIdNegocio(citaBody.getIdNegocio());
        cita.setNegocio(negocio);

        cita.setCliente(null); // cita rápida

        Usuario barbero = new Usuario();
        barbero.setId(citaBody.getIdUsuario());
        cita.setEmpleado(barbero);

        cita.setEstado(EstadoCita.AGENDADA);
        cita.setTipoCita(TipoCita.RAPIDA);

        this.citaRepository.save(cita);

        return "✅ Cita rápida agendada\n\n" +
                "📅 Fecha: " + cita.getFecha() +
                "\n⏰ Hora: " + cita.getHoraInicio() +
                "\n💈 Servicio: " + ser.getNombre();
    }

    @Override
    public String guardarCitaSesion(SesionWhatsapp sesion) {

        Servicio ser = this.iServicioQuery.findByServicio(sesion.getServicioId());
        Negocio negocio = this.negocioQuery.encontrarNegocioById(sesion.getSucursalId());
        LocalTime horaFin = sesion.getHora().plusMinutes(ser.getDuracionMinutos());
        Cliente cliente = this.clienteQuery.obtenerClienteByNumero(sesion.getTelefono());
        Usuario barbero = this.empleadoQuery.obtenerEmpleadoById(sesion.getEmpleadoId());

        Cita cita = new Cita();
        cita.setFecha(sesion.getFechaCreacion());
        cita.setHoraInicio(sesion.getHora());
        cita.setHoraFin(horaFin);
        cita.setServicio(ser);
        cita.setNegocio(negocio);
        cita.setCliente(cliente);
        cita.setEstado(EstadoCita.AGENDADA);
        cita.setEmpleado(barbero);
        cita.setTipoCita(TipoCita.NORMAL);
        this.citaRepository.save(cita);

        String respuesta = "✅ Cita agendada. Muchas Gracias Por Su Preferencia\n\n"
                + "📅 Fecha: " + cita.getFecha()
                + "\n⏰ Hora: " + sesion.getHora()
                + "\n💈 Servicio: " + ser.getNombre();
        return respuesta;

    }

    @Override
    public String guardarCita(SesionWhatsapp sesion, LocalTime horaSelect) {

        Servicio ser = this.iServicioQuery.findByServicio(sesion.getServicioId());
        Negocio negocio = this.negocioQuery.encontrarNegocioById(sesion.getSucursalId());
        LocalTime horaFin = horaSelect.plusMinutes(ser.getDuracionMinutos());
        Cliente cliente = this.clienteQuery.obtenerClienteById(sesion.getClienteId());
        Usuario barbero = this.empleadoQuery.obtenerEmpleadoById(sesion.getEmpleadoId());

        Cita cita = new Cita();
        cita.setFecha(LocalDate.now());
        cita.setHoraInicio(horaSelect);
        cita.setHoraFin(horaFin);
        cita.setServicio(ser);
        cita.setNegocio(negocio);
        cita.setCliente(cliente);
        cita.setEstado(EstadoCita.AGENDADA);
        cita.setEmpleado(barbero);
        // FALTA GUARDAR EL BARBERO
        this.citaRepository.save(cita);
        String respuesta = "✅ Cita agendada\n\n"
                + "📅 Fecha: " + cita.getFecha()
                + "\n⏰ Hora: " + horaSelect
                + "\n💈 Servicio: " + ser.getNombre();
        return respuesta;
    }
}
