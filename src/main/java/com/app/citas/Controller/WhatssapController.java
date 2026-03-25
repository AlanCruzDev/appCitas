package com.app.citas.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.citas.Entity.Cita;
import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Services.cita.ICitaQuery;
import com.app.citas.Services.sessiones.ISesionMutant;
import com.app.citas.Services.sessiones.ISesionQuery;
import com.app.citas.config.BotStateFactory;
import com.app.citas.config.FraseParserPro;
import com.app.citas.config.IntentHandlerPro;

@RestController
@RequestMapping("api/whatssap")
public class WhatssapController {

        @Autowired
        private IntentHandlerPro intentHandler;

        @Autowired
        private FraseParserPro fraseParser;

        @Autowired
        private ISesionQuery sesionQuery;

        @Autowired
        private ISesionMutant sesionMutant;

        @Autowired
        private BotStateFactory botstateFactory;

        @Autowired
        private ICitaQuery citaQuery;

        @PostMapping(value = "/menu", produces = "application/xml")
        public String recibirMensaje(
                        @RequestParam("From") String from,
                        @RequestParam("To") String to,
                        @RequestParam("Body") String body) {
                SesionWhatsapp sesion = this.sesionQuery.obtenerSesion(from);

                String respuesta;
                if (sesion.getMensajeSistema() != null) {
                        sesionMutant.reiniciarSesion(sesion);
                        return buildResponse(
                                        "⏳ Tu sesión expiró por inactividad.\n\n" +
                                                        "Empecemos de nuevo 😊\n\n",
                                        sesion);
                }

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
                String dectar = this.intentHandler.detectarIntento(body);

                if (dectar.equals("AGENDAR")) {
                        List<Cita> citas = this.citaQuery.verficarCantidadCitasAgendadas(from,
                                        fecha == null ? LocalDate.now() : fecha);
                        if (citas.size() > 0) {
                                Cita cita = citas.get(0);
                                return buildResponse(
                                                "⚠️ Ya tienes una cita hoy a las " + cita.getHoraInicio() + "\n\n" +
                                                                "Reagendar\n" +
                                                                "Cancelar\n",
                                                sesion);
                        }
                        sesion.setEstado(EstadoBot.SELECCION_SUCURSAL);
                }

                BotState estado = botstateFactory.obtenerEstados(sesion.getEstado());
                respuesta = estado.procesar(from, to, body, sesion);

                while (respuesta == null || respuesta.isEmpty()) {
                        estado = botstateFactory.obtenerEstados(sesion.getEstado());
                        respuesta = estado.procesar(from, to, "", sesion);
                }

                return buildResponse(respuesta, sesion);
        }

        private String buildResponse(String mensaje, SesionWhatsapp sesion) {
                sesionMutant.guardarSesion(sesion);
                return "<Response><Message>" +
                                mensaje +
                                "</Message></Response>";
        }
}