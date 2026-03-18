package com.app.citas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.BotState;
import com.app.citas.Services.cita.CitaMutations;

@Component
public class CitaState implements BotState {

    @Autowired
    private CitaMutations citaMutations;

    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {

        return "";
    }

}
