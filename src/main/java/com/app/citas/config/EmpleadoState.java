package com.app.citas.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Entity.Usuario;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Mapper.ConstrucionMensaje;
import com.app.citas.Mapper.Model.ServiciosModel;
import com.app.citas.Services.servicios.IServicioQuery;
import com.app.citas.Services.usuario.EmpleadoQuery;

@Component
public class EmpleadoState implements BotState {

    @Autowired
    private EmpleadoQuery empleadoQuery;

    @Autowired
    private IServicioQuery servicioQuery;

    @Autowired
    private ConstrucionMensaje construcionMensaje;

    @Autowired
    private BotValidador botValidador;

    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {

        List<Usuario> empleados = empleadoQuery.obtenerEmpleadosBySucursal(sesion.getSucursalId());
        Integer opcionValidate = this.botValidador.validarOpcion(body, empleados.size());

        if (opcionValidate == null) {
            List<String> nombres = empleados.stream().map(Usuario::getNombre).toList();
            return this.construcionMensaje.ConstruirLista("Selecciona un empleado:", nombres);
        }

        Usuario empleadoSelect = empleados.get(opcionValidate - 1);
        sesion.setEmpleadoId(empleadoSelect.getId());
        sesion.setEstado(EstadoBot.SELECCION_EMPLEADO);
        return generarMapeoServicios(sesion.getSucursalId(), sesion.getEmpleadoId());

    }

    private String generarMapeoServicios(long IdNegocio, Long idEmpleado) {
        List<ServiciosModel> servicios = this.servicioQuery.obtenerServiciosByNegocio(IdNegocio,
                idEmpleado);
        StringBuilder mensajeServicio = new StringBuilder();
        mensajeServicio.append("Selecciona un servicio:\n\n");
        int i = 1;
        for (ServiciosModel s : servicios) {
            mensajeServicio.append(i)
                    .append("️⃣ ")
                    .append(s.getNomServicio())
                    .append(" - Precio: ")
                    .append(s.getPrecio())
                    .append("\n");
            i++;
        }
        return mensajeServicio.toString();
    }
}
