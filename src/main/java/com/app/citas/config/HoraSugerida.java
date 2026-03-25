package com.app.citas.config;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Services.cita.ICitaQuery;
import com.app.citas.Util.Util;

@Component
public class HoraSugerida implements BotState {

    @Autowired
    private ICitaQuery citaQuery;

    @Autowired
    private Util util;

    @Autowired
    private BotValidador botValidador;

    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {

        List<LocalTime> horarios = this.citaQuery.obtenerHorariosDisponibles(sesion);
        Integer opcionValidate = this.botValidador.validarOpcion(body, horarios.size());

        if (opcionValidate == null) {
            StringBuilder mensajeHora = new StringBuilder();
            mensajeHora.append("❌ Opción inválida\n\n");
            mensajeHora.append("Selecciona un horario disponible:\n");
            for (int i = 0; i < horarios.size(); i++) {
                mensajeHora.append(i + 1)
                        .append("️⃣ ")
                        .append(this.util.formatearHora(horarios.get(i)))
                        .append("\n");
            }
            return mensajeHora.toString();
        }

        LocalTime horaSelect = horarios.get(opcionValidate - 1);
        sesion.setHora(horaSelect);
        sesion.setEstado(EstadoBot.CREAR_CITA);
        return null;

    }

}
