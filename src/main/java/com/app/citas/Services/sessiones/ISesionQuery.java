package com.app.citas.Services.sessiones;

import com.app.citas.Entity.SesionWhatsapp;

public interface ISesionQuery {
    public SesionWhatsapp obtenerSesion(String telefono);
    public boolean sesionExpirada(SesionWhatsapp sesion);
}
