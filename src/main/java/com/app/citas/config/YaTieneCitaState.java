package com.app.citas.config;

import org.springframework.stereotype.Component;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.BotState;

@Component
public class YaTieneCitaState implements BotState {

    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'procesar'");
    }

}
