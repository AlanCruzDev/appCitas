package com.app.citas.config;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Entity.Usuario;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Mapper.ConstrucionMensaje;
import com.app.citas.Services.cita.ICitaQuery;
import com.app.citas.Services.usuario.EmpleadoQuery;

@Component
public class EmpleadoState implements BotState {

    @Autowired
    private EmpleadoQuery empleadoQuery;

    @Autowired
    private ICitaQuery citaQuery;

    @Autowired
    private ConstrucionMensaje construcionMensaje;

    @Autowired
    private BotValidador botValidador;

    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {

        String respuesta = "";
        List<Usuario> empleados = empleadoQuery.obtenerEmpleadosBySucursal(sesion.getSucursalId());
        Integer opcionValidate = this.botValidador.validarOpcion(body, empleados.size());
        if (opcionValidate == null) {
            List<String> nombres = empleados.stream().map(Usuario::getNombre).toList();
            return this.construcionMensaje.ConstruirLista("Selecciona un empleado:", nombres);
        }

        Usuario empleadoSelect = empleados.get(opcionValidate - 1);
        sesion.setEmpleadoId(empleadoSelect.getId());

        List<LocalTime> horarios = citaQuery.obtenerHorariosDisponibles(sesion);
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

        if (sesion.getFechaCreacion() != null && sesion.getHora() != null) {
            sesion.setEstado(EstadoBot.SELECCION_FECHA);

        } else {
            sesion.setEstado(EstadoBot.CREAR_CITA);
        }
        return respuesta;
    }

}
