package com.app.citas.config;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Entity.Usuario;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Services.cita.ICitaQuery;
import com.app.citas.Services.usuario.EmpleadoQuery;


@Component
public class EmpleadoState implements BotState{


    @Autowired
    private EmpleadoQuery empleadoQuery;

    @Autowired
    private ICitaQuery citaQuery;


    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {
        
        String respuesta ="";
        int opcionEmpleado = Integer.parseInt(body.trim());
        List<Usuario>empleados =
                        empleadoQuery.obtenerEmpleadosBySucursal(
                                sesion.getSucursalId());
        Usuario empleadoSelect =
                        empleados.get(opcionEmpleado - 1);
        sesion.setEmpleadoId(empleadoSelect.getId());
        List<LocalTime> horarios =
                        citaQuery.obtenerHorariosDisponibles(sesion);
        
        StringBuilder mensajeHora = new StringBuilder();
        mensajeHora.append("Selecciona un horario disponible:\n\n");
        int k = 1;
                for (LocalTime hora : horarios) {
                    mensajeHora.append(k)
                            .append("️⃣ ")
                            .append(hora)
                            .append("\n");
                    k++;
                }
        respuesta = mensajeHora.toString();

        sesion.setEstado(EstadoBot.SELECCION_FECHA);
        return respuesta;
    }
    
}
