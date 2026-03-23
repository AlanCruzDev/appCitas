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
import com.app.citas.Util.Util;

@Component
public class CitaState implements BotState {

    @Autowired
    private ICitaQuery citaQuery;

    @Autowired
    private CitaMutations citaMutations;

    @Autowired
    private Util util;

    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {
        List<LocalTime> disponibles = this.citaQuery.procesarHoraUsuario(sesion);

        if (disponibles.isEmpty()) {
            return "❌ No hay horarios disponibles hoy";
        }

        if (disponibles.contains(sesion.getHora())) {
            // CREAMOS LA CITA
            return this.citaMutations.guardarCitaSesion(sesion);
        }

        List<LocalTime> sugerencias = this.util.sugerirDosMejoresSlots(sesion.getHora(), disponibles);

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("⏰ No tengo disponible a las ")
                .append(this.util.formatearHora(sesion.getHora()))
                .append("\n\nPero puedo agendarte a:\n\n");

        for (int i = 0; i < sugerencias.size(); i++) {
            mensaje.append(i + 1)
                    .append("️⃣ ")
                    .append(this.util.formatearHora(sugerencias.get(i)))
                    .append("\n");
        }

        mensaje.append("\nResponde con el número que prefieras 🙌");

        sesion.setHorariosSugeridos(sugerencias);
        sesion.setEstado(EstadoBot.SELECCION_HORA_SUGERIDA);
        return mensaje.toString();
    }
}
