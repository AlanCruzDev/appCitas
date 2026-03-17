package com.app.citas.Services.sessiones;

import com.app.citas.Entity.SesionWhatsapp;

public interface ISesionMutant {

    public SesionWhatsapp crearSesionNueva(String telefono);
    public void guardarSesion(SesionWhatsapp sesion);
    public SesionWhatsapp reiniciarSesion(SesionWhatsapp sesion);
}
