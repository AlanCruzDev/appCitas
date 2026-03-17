package com.app.citas.Estados;

import com.app.citas.Entity.SesionWhatsapp;

public interface BotState {
    
    String procesar(
        String from,
        String to,
        String body,
        SesionWhatsapp sesion
    );
}
