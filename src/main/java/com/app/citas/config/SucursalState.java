package com.app.citas.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Mapper.Model.NegocioModel;
import com.app.citas.Mapper.Model.ServiciosModel;
import com.app.citas.Services.negocio.INegocioQuery;
import com.app.citas.Services.servicios.IServicioQuery;


@Component
public class SucursalState implements BotState{

    @Autowired
    private INegocioQuery negocioQuery;

    @Autowired
    private IServicioQuery iServicioQuery;
    
    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {
        String respuesta = "";
        int opcionSucursal = Integer.parseInt(body.trim());

        List<NegocioModel> sucursales =
                        negocioQuery.encontrarNegocioByNumero(to);
        NegocioModel negocioSelect =
                        sucursales.get(opcionSucursal - 1);

        sesion.setSucursalId(negocioSelect.getIdNegocio());

        List<ServiciosModel> servicios =
                        iServicioQuery.obtenerServiciosByNegocio(
                                negocioSelect.getIdNegocio());
        
        
        StringBuilder mensajeServicio = new StringBuilder();
        mensajeServicio.append("Selecciona un servicio:\n\n");

        int i = 1;
            for (ServiciosModel s : servicios) {
                mensajeServicio.append(i)
                            .append("️⃣ ")
                            .append(s.getNombre())
                            .append(" - Precio: ")
                            .append(s.getPrecio())
                            .append("\n");
                    i++;
            }

        respuesta = mensajeServicio.toString();
        sesion.setEstado(EstadoBot.SELECCION_SERVICIO);
        return respuesta;
    }


}
