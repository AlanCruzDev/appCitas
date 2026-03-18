package com.app.citas.config;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class IntentHandlerPro {

    private static final Map<String, String> INTENTS = new HashMap<>();
    private static final double THRESHOLD = 0.75;

    static {
        INTENTS.put("cita", "AGENDAR");
        INTENTS.put("agendar", "AGENDAR");
        INTENTS.put("reservar", "AGENDAR");

        INTENTS.put("cancelar", "CANCELAR");
        INTENTS.put("eliminar", "CANCELAR");

        INTENTS.put("menu", "MENU");
        INTENTS.put("inicio", "MENU");
    }

    public String detectarIntento(String input) {
        input = normalizar(input);
        for (Map.Entry<String, String> entry : INTENTS.entrySet()) {
            if (contienePalabraParecida(input, entry.getKey())) {
                return entry.getValue();
            }
        }
        return "NONE";
    }

    private String normalizar(String input) {
        input = input.toLowerCase().trim();
        input = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return input;
    }

    private boolean contienePalabraParecida(String input, String palabra) {

        if (input.contains(palabra)) {
            return true;
        }

        for (String token : input.split(" ")) {
            if (similitud(token, palabra) >= THRESHOLD) {
                return true;
            }
        }

        return false;
    }

    private double similitud(String a, String b) {
        int distancia = distanciaLevenshtein(a, b);
        int maxLen = Math.max(a.length(), b.length());

        if (maxLen == 0)
            return 1.0;

        return 1.0 - (double) distancia / maxLen;
    }

    private int distanciaLevenshtein(String a, String b) {

        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {

                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    int costo = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;

                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + costo);
                }
            }
        }

        return dp[a.length()][b.length()];
    }

}
