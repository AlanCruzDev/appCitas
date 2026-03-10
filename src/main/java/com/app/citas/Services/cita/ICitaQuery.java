package com.app.citas.Services.cita;

import java.time.LocalTime;
import java.util.List;

import com.app.citas.Estados.SesionUsuario;

public interface ICitaQuery {
    
    public List<LocalTime> obtenerHorariosDisponibles(SesionUsuario sesionUsuario);
    
}
