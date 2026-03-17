package com.app.citas.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Entity.Usuario;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Mapper.Model.ServiciosModel;
import com.app.citas.Services.servicios.IServicioQuery;
import com.app.citas.Services.usuario.EmpleadoQuery;

@Component
public class ServicioState implements BotState{


    @Autowired
    private IServicioQuery servicioQuery;

    @Autowired
    private EmpleadoQuery empleadoQuery;


    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {
        String respuesta = "";
        int opcionServicio = Integer.parseInt(body.trim());

        List<ServiciosModel> serviciosList = servicioQuery.obtenerServiciosByNegocio(sesion.getSucursalId());
        ServiciosModel servicioSelect =
                        serviciosList.get(opcionServicio - 1);
        sesion.setServicioId(servicioSelect.getIdServicio());

        List<Usuario> empleados =
                        empleadoQuery.obtenerEmpleadosBySucursal(
                                sesion.getSucursalId());

        StringBuilder mensajeEmpleado = new StringBuilder();
                mensajeEmpleado.append("Selecciona un empleado:\n\n");

        int j = 1;
        for (Usuario user : empleados) {
                mensajeEmpleado.append(j)
                .append("️⃣ ")
                .append(user.getNombre())
                .append("\n");
                    j++;
        }
        respuesta = mensajeEmpleado.toString();

        sesion.setEstado(EstadoBot.SELECCION_EMPLEADO);
        return respuesta; 
    }
    
}
