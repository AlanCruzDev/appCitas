package com.app.citas.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Mapper.Model.NegocioModel;
import com.app.citas.Services.negocio.INegocioQuery;
import com.app.citas.Services.sessiones.ISesionMutant;
import com.app.citas.Services.sessiones.ISesionQuery;
import com.app.citas.config.BotStateFactory;
import com.app.citas.config.FraseParserPro;

@RestController
@RequestMapping("api/whatssap")
public class WhatssapController {

        // @Autowired
        // private IntentHandlerPro intentHandler;

        @Autowired
        private FraseParserPro fraseParser;

        @Autowired
        private ISesionQuery sesionQuery;

        @Autowired
        private ISesionMutant sesionMutant;

        @Autowired
        private BotStateFactory botstateFactory;

        @Autowired
        private INegocioQuery negocioQuery;

        @PostMapping(value = "/menu", produces = "application/xml")
        public String recibirMensaje(
                        @RequestParam("From") String from,
                        @RequestParam("To") String to,
                        @RequestParam("Body") String body) {
                SesionWhatsapp sesion = this.sesionQuery.obtenerSesion(from);

                // validamos si ya expiro el mensaje sesion.setMensajeSistema("⏳ Tu sesión
                // expiró por inactividad.\n\nIniciemos nuevamente.");

                String respuesta;

                /*
                 * String intent = intentHandler.detectarIntento(body);
                 * 
                 * if (intent.equals("AGENDAR") && sesion.getEstado() == EstadoBot.MENU) {
                 * sesion.setEstado(EstadoBot.SELECCION_SUCURSAL);
                 * respuesta = "Perfecto 👍 vamos a agendar\n\n" + construirSucursales(to);
                 * return buildResponse(respuesta, sesion);
                 * }
                 */

                LocalDate fecha = fraseParser.detectarFechaPro(body);

                if (fecha == null) {
                        fecha = fraseParser.detectarFecha(body);
                }
                LocalTime hora = fraseParser.detectarHoraPro(body);

                if (fecha != null) {
                        sesion.setFechaCreacion(fecha);
                }

                if (hora != null) {
                        sesion.setHora(hora);
                }

                if (fecha != null && hora != null) {
                        sesion.setEstado(EstadoBot.SELECCION_SUCURSAL);

                        return buildResponse(
                                        "📅 Perfecto\n\n" +
                                                        "Fecha: " + fecha +
                                                        "\nHora: " + hora +
                                                        "Perfecto 👍  "
                                                        + construirSucursales(to),
                                        sesion);

                }

                // 🔥 3. Flujo normal (State Machine)
                BotState estado = botstateFactory.obtenerEstados(sesion.getEstado());
                respuesta = estado.procesar(from, to, body, sesion);

                return buildResponse(respuesta, sesion);

        }

        private String buildResponse(String mensaje, SesionWhatsapp sesion) {
                sesionMutant.guardarSesion(sesion);
                return "<Response><Message>" +
                                mensaje +
                                "</Message></Response>";
        }

        private String construirSucursales(String to) {
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
                return mensaje.toString();
        }
}