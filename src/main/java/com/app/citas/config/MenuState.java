package com.app.citas.config;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.citas.Entity.Cliente;
import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Mapper.Model.NegocioModel;
import com.app.citas.Services.clientes.ClienteMutations;
import com.app.citas.Services.clientes.ClienteQuery;
import com.app.citas.Services.negocio.INegocioQuery;

@Component
public class MenuState implements BotState {

    @Autowired
    private ClienteQuery clienteQuery;

    @Autowired
    private ClienteMutations clienteMutations;

    @Autowired
    private INegocioQuery negocioQuery;

    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {

        String respuesta = "";

        if (body.trim().equals("1")) {
            sesion.setEstado(EstadoBot.SELECCION_SUCURSAL);
            List<NegocioModel> sucursales = negocioQuery.encontrarNegocioByNumero(to);
            StringBuilder mensaje = new StringBuilder();

            mensaje.append("Selecciona una sucursal:\n\n");
            int i = 1;
            for (NegocioModel s : sucursales) {
                mensaje.append(i)
                        .append("️⃣ ")
                        .append(s.getNombre())
                        .append("\n");
                i++;
            }
            respuesta = mensaje.toString();

            // CAMBIAMOS DE CICLO MENU A SUCURSAL
            sesion.setEstado(EstadoBot.SELECCION_SUCURSAL);
        } else {

            Cliente cliente = clienteQuery.obtenerClienteByNumero(from);
            if (Objects.isNull(cliente)) {
                cliente = clienteMutations.guardarCliente(from);
            }

            // ACTUALIZAMOS LA SESSION
            sesion.setClienteId(cliente.getId());
            respuesta = "Hola Bienvenido 👋\n\n" +
                    "1️⃣ Agendar cita\n" +
                    "2️⃣ Cancelar cita\n" +
                    "3️⃣ Ver horarios";
        }
        return respuesta;
    }
}
