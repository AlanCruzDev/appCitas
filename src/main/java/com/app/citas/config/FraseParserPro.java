package com.app.citas.config;

import java.text.Normalizer;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class FraseParserPro {

    public LocalDate detectarFechaPro(String input) {

        input = normalizar(input);
        Pattern formatoNumerico = Pattern.compile("(\\d{1,2})/(\\d{1,2})");
        Matcher m1 = formatoNumerico.matcher(input);

        if (m1.find()) {
            int dia = Integer.parseInt(m1.group(1));
            int mes = Integer.parseInt(m1.group(2));
            return LocalDate.of(LocalDate.now().getYear(), mes, dia);
        }

        Pattern formatoTexto = Pattern.compile("(\\d{1,2})\\s+de\\s+(\\w+)");
        Matcher m2 = formatoTexto.matcher(input);

        if (m2.find()) {
            int dia = Integer.parseInt(m2.group(1));
            Month mes = obtenerMes(m2.group(2));
            if (mes != null) {
                return LocalDate.of(LocalDate.now().getYear(), mes, dia);
            }
        }

        return detectarFecha(input);
    }

    private Month obtenerMes(String mesTexto) {

        return switch (mesTexto) {
            case "enero" -> Month.JANUARY;
            case "febrero" -> Month.FEBRUARY;
            case "marzo" -> Month.MARCH;
            case "abril" -> Month.APRIL;
            case "mayo" -> Month.MAY;
            case "junio" -> Month.JUNE;
            case "julio" -> Month.JULY;
            case "agosto" -> Month.AUGUST;
            case "septiembre" -> Month.SEPTEMBER;
            case "octubre" -> Month.OCTOBER;
            case "noviembre" -> Month.NOVEMBER;
            case "diciembre" -> Month.DECEMBER;
            default -> null;
        };
    }

    public LocalDate detectarFecha(String input) {

        input = normalizar(input);

        if (input.contains("hoy")) {
            return LocalDate.now();
        }

        if (input.contains("mañana")) {
            return LocalDate.now().plusDays(1);
        }

        if (input.contains("pasado mañana")) {
            return LocalDate.now().plusDays(2);
        }

        if (input.contains("lunes"))
            return siguienteDia(DayOfWeek.MONDAY);
        if (input.contains("martes"))
            return siguienteDia(DayOfWeek.TUESDAY);
        if (input.contains("miercoles"))
            return siguienteDia(DayOfWeek.WEDNESDAY);
        if (input.contains("jueves"))
            return siguienteDia(DayOfWeek.THURSDAY);
        if (input.contains("viernes"))
            return siguienteDia(DayOfWeek.FRIDAY);
        if (input.contains("sabado"))
            return siguienteDia(DayOfWeek.SATURDAY);
        if (input.contains("domingo"))
            return siguienteDia(DayOfWeek.SUNDAY);
        return null;
    }

    public LocalTime detectarHoraPro(String input) {

        input = normalizar(input);

        // 🔥 SOLO detectar hora después de "a las"
        Pattern pattern = Pattern.compile("a\\s+las\\s+(\\d{1,2})(:(\\d{2}))?");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {

            int hora = Integer.parseInt(matcher.group(1));
            int min = matcher.group(3) != null
                    ? Integer.parseInt(matcher.group(3))
                    : 0;

            // 🔥 CONTEXTO
            if (input.contains("tarde") && hora < 12)
                hora += 12;

            if (input.contains("pm") && hora < 12)
                hora += 12;

            if (input.contains("am") && hora == 12)
                hora = 0;

            // 🔥 VALIDACIÓN EXTRA
            if (hora < 0 || hora > 23) {
                return null;
            }

            return LocalTime.of(hora, min);
        }

        return null;
    }

    private LocalDate siguienteDia(DayOfWeek dia) {
        return LocalDate.now().with(TemporalAdjusters.next(dia));
    }

    private String normalizar(String input) {
        input = input.toLowerCase().trim();

        input = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        return input;
    }

}
