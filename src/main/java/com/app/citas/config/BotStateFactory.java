package com.app.citas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;

@Component
public class BotStateFactory {

    @Autowired
    private SucursalState sucursalState;

    @Autowired
    private ServicioState servicioState;

    @Autowired
    private EmpleadoState empleadoState;

    @Autowired
    private CitaState citaState;

    @Autowired
    private FechaState fechaState;

    public BotState obtenerEstados(EstadoBot estadoBot) {
        return switch (estadoBot) {
            case SELECCION_SUCURSAL -> sucursalState;
            case SELECCION_SERVICIO -> servicioState;
            case SELECCION_EMPLEADO -> empleadoState;
            case SELECCION_FECHA -> fechaState;
            case CREAR_CITA -> citaState;
            default -> servicioState;
        };
    }

}
