package com.app.citas.Services.cita;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.Cita;
import com.app.citas.Entity.Cliente;
import com.app.citas.Entity.EstadoCita;
import com.app.citas.Entity.Negocio;
import com.app.citas.Entity.Servicio;
import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Entity.Usuario;
import com.app.citas.Repository.CitaRepository;
import com.app.citas.Services.clientes.ClienteQuery;
import com.app.citas.Services.negocio.INegocioQuery;
import com.app.citas.Services.servicios.IServicioQuery;
import com.app.citas.Services.usuario.EmpleadoQuery;

@Service
@Transactional
public class CitaMutationsImpl implements CitaMutations {

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
