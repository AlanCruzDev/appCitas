package com.app.citas.Util;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class Util {

    public List<LocalTime> sugerirDosMejoresSlots(LocalTime horaUsuario, List<LocalTime> disponibles) {
        return disponibles.stream()
                .sorted((a, b) -> Long.compare(
                        Math.abs(java.time.Duration.between(a, horaUsuario).toMinutes()),
                        Math.abs(java.time.Duration.between(b, horaUsuario).toMinutes())))
                .limit(2)
                .toList();
    }

    public String formatearHora(LocalTime hora) {
        return hora.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
    }

}
