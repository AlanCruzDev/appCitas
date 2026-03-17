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


    @Override
    public String procesar(String from, String to, String body,
            SesionWhatsapp sesion) {

        String respuesta = "";
        int opcionHora = Integer.parseInt(body.trim());
        List<LocalTime> horariosLista = citaQuery.obtenerHorariosDisponibles(sesion);
        LocalTime horaSelect = horariosLista.get(opcionHora - 1);
        respuesta = citaMutations.guardarCita(sesion, horaSelect);
        sesion.setEstado(EstadoBot.MENU);
        return respuesta;
    }

}
