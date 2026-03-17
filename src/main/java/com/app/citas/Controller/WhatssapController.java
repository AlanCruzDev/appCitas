package com.app.citas.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.BotState;
import com.app.citas.Services.sessiones.ISesionMutant;
import com.app.citas.Services.sessiones.ISesionQuery;
import com.app.citas.config.BotStateFactory;

@RestController
@RequestMapping("api/whatssap")
public class WhatssapController {

    @Autowired
    private ISesionQuery sesionQuery;

    @Autowired
    private ISesionMutant sesionMutant;
    
    @Autowired
    private BotStateFactory botstateFactory;

    @PostMapping(value = "/menu", produces = "application/xml")
    public String recibirMensaje(
        @RequestParam("From") String from, 
        @RequestParam("To") String to, 
        @RequestParam("Body") String body) {        
        SesionWhatsapp sesion =this.sesionQuery.obtenerSesion(from);
        BotState estado = botstateFactory.obtenerEstados(sesion.getEstado());
        String respuesta = estado.procesar(from, to, body, sesion);
        this.sesionMutant.guardarSesion(sesion);

        return "<Response><Message>"
            + respuesta +
            "</Message></Response>";
        }
}