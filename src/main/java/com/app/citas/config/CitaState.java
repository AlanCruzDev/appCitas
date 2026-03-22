package com.app.citas.config;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Services.cita.CitaMutations;
import com.app.citas.Services.cita.ICitaQuery;

@Component
public class CitaState implements BotState {

    @Autowired
    private CitaMutations citaMutations;

    @Autowired
    private ICitaQuery citaQuery;

    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {

        String respuesta = "";
        boolean disponible = this.citaQuery.cupoDisponible(sesion);

        if (disponible) {
            respuesta = this.citaMutations.guardarCitaSesion(sesion);
        } else {
            respuesta = crearListaHorarios(sesion);
            sesion.setEstado(EstadoBot.SELECCION_FECHA);
            return respuesta;
        }

        return respuesta;
    }

    private String crearListaHorarios(SesionWhatsapp sesion) {
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
        return mensajeHora.toString();
    }

}
