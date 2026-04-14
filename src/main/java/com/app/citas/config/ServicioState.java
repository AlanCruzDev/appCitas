package com.app.citas.config;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Mapper.ConstrucionMensaje;
import com.app.citas.Mapper.Model.ServiciosModel;
import com.app.citas.Services.cita.ICitaQuery;
import com.app.citas.Services.servicios.IServicioQuery;

@Component
public class ServicioState implements BotState {

    @Autowired
    private IServicioQuery servicioQuery;

    @Autowired
    private ConstrucionMensaje construcionMensaje;

    @Autowired
    private BotValidador botValidador;

    @Autowired
    private ICitaQuery citaQuery;

    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {

        String respuesta = "";
        List<ServiciosModel> serviciosList = servicioQuery.obtenerServiciosByNegocio(sesion.getSucursalId(),
                sesion.getEmpleadoId());

        Integer opcionValidate = this.botValidador.validarOpcion(body, serviciosList.size());
        if (opcionValidate == null) {
            List<String> nombres = serviciosList.stream().map(ServiciosModel::getNomServicio).toList();
            return this.construcionMensaje.ConstruirLista("Selecciona un servicio:", nombres);
        }

        ServiciosModel servicioSelect = serviciosList.get(opcionValidate - 1);
        sesion.setServicioId(servicioSelect.getIdServicio());
        if (sesion.getFechaCreacion() == null || sesion.getHora() == null) {
            respuesta = crearListaHorarios(sesion);
            sesion.setEstado(EstadoBot.SELECCION_FECHA);
            return respuesta;
        } else {
            sesion.setEstado(EstadoBot.CREAR_CITA);
            return null;
        }

    }

    private String crearListaHorarios(SesionWhatsapp sesion) {
        List<LocalTime> horarios = citaQuery.obtenerHorariosDisponibles(sesion);
        StringBuilder mensajeHora = new StringBuilder();
        mensajeHora.append("Selecciona un horario disponible:\n\n");
        int k = 1;
        for (LocalTime hora : horarios) {
            mensajeHora.append(k)
                    .append("️⃣ ")
                    .append(hora)
                    .append("\n");
            k++;
        }
        return mensajeHora.toString();
    }

}
