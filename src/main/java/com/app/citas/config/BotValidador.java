package com.app.citas.config;

import org.springframework.stereotype.Component;

@Component
public class BotValidador {

    public Integer validarOpcion(String body, int max) {
        try {

            int opcion = Integer.parseInt(body.trim());
            if (opcion < 1 || opcion > max) {
                return null;
            }
            return opcion;
        } catch (Exception ex) {
            return null;
        }
    }

}
