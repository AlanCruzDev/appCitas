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
public class FechaState implements BotState {

    @Autowired
    private ICitaQuery citaQuery;

    @Autowired
    private CitaMutations citaMutations;

    @Autowired
    private BotValidador botValidador;

    @Override
    public String procesar(String from, String to, String body,
            SesionWhatsapp sesion) {
        String respuesta = "";

        List<LocalTime> horariosLista = citaQuery.obtenerHorariosDisponibles(sesion);
        Integer opcionValidate = this.botValidador.validarOpcion(body, horariosLista.size());

        if (opcionValidate == null) {
            StringBuilder mensajeHora = new StringBuilder();
            mensajeHora.append("❌ Opción inválida\n\n");
            mensajeHora.append("Selecciona un horario disponible:\n");
            int k = 1;
            for (LocalTime hora : horariosLista) {
                mensajeHora.append(k)
                        .append("️⃣ ")
                        .append(hora)
                        .append("\n");
                k++;
            }
            return mensajeHora.toString();
        }

        LocalTime horaSelect = horariosLista.get(opcionValidate - 1);
        respuesta = citaMutations.guardarCita(sesion, horaSelect);
        sesion.setEstado(EstadoBot.MENU);
        return respuesta;
    }

}
