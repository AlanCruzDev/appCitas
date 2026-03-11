package com.app.citas.Services.cita;

import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.Cita;
import com.app.citas.Entity.Cliente;
import com.app.citas.Entity.EstadoCita;
import com.app.citas.Entity.Negocio;
import com.app.citas.Entity.Servicio;
import com.app.citas.Estados.SesionUsuario;
import com.app.citas.Services.clientes.ClienteQuery;
import com.app.citas.Services.negocio.INegocioQuery;
import com.app.citas.Services.servicios.IServicioQuery;

@Service
@Transactional
public class CitaMutationsImpl implements CitaMutations{

    @Autowired
    private IServicioQuery iServicioQuery;

    @Autowired
    private INegocioQuery negocioQuery;

    @Autowired
    private ClienteQuery clienteQuery;

    @Override
    public String guardarCita(SesionUsuario serviciosModel, LocalTime horaSelect) {
        
        Servicio ser = this.iServicioQuery.findByServicio(serviciosModel.getServicioId());
        Negocio negocio = this.negocioQuery.encontrarNegocioById(serviciosModel.getSucursalId());
        LocalTime horaFin = horaSelect.plusMinutes(ser.getDuracionMinutos());
        Cliente cliente = this.clienteQuery.obtenerClienteByNumero(serviciosModel.getTelefono());

        Cita cita = new Cita();
        cita.setFecha(serviciosModel.getFecha());
        cita.setHoraInicio(horaSelect);
        cita.setHoraFin(horaFin);
        cita.setServicio(ser);
        cita.setNegocio(negocio);
        cita.setCliente(cliente);
        cita.setEstado(EstadoCita.AGENDADA);

        // FALTA GUARDAR EL BARBERO

        String respuesta = "✅ Cita agendada\n\n"
            + "📅 Fecha: " + serviciosModel.getFecha()
            + "\n⏰ Hora: " + horaSelect
            + "\n💈 Servicio: " + ser.getNombre();
        return respuesta;
    }

}
