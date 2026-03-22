package com.app.citas.Mapper;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ConstrucionMensaje {

    public String ConstruirLista(String titulo, List<String> items) {
        StringBuilder mensaje = new StringBuilder();
        mensaje.append(titulo).append("\n\n");
        int i = 1;
        for (String item : items) {
            mensaje.append(i)
                    .append("️⃣ ")
                    .append(item)
                    .append("\n");
            i++;
        }
        return mensaje.toString();
    }

}
