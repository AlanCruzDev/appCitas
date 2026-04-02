package com.app.citas.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Mapper.ConstrucionMensaje;
import com.app.citas.Mapper.Model.NegocioModel;
import com.app.citas.Mapper.Model.ServiciosModel;
import com.app.citas.Services.negocio.INegocioQuery;

@Component
public class SucursalState implements BotState {

    @Autowired
    private INegocioQuery negocioQuery;

    // @Autowired
    // private IServicioQuery iServicioQuery;

    @Autowired
    private BotValidador botValidador;

    @Autowired
    private ConstrucionMensaje construcionMensaje;

    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {
        String respuesta = "";
        Integer opcionValidate = null;

        List<NegocioModel> sucursales = negocioQuery.encontrarNegocioByNumero(to);
        if (sucursales.size() == 1) {
            sesion.setSucursalId(sucursales.get(0).getIdNegocio());
            respuesta = generarMapeoServicios(sesion.getSucursalId());
            sesion.setEstado(EstadoBot.SELECCION_SERVICIO);
            return respuesta;

        } else {
            opcionValidate = this.botValidador.validarOpcion(body, sucursales.size());
            if (opcionValidate == null) {
                List<String> nombres = sucursales.stream().map(NegocioModel::getNombre).toList();
                return this.construcionMensaje.ConstruirLista("Selecciona una sucursal:", nombres);
            }
        }

        NegocioModel negocioSelect = selecionarNegocio(opcionValidate, sucursales);
        sesion.setSucursalId(negocioSelect.getIdNegocio());
        respuesta = generarMapeoServicios(negocioSelect.getIdNegocio());
        sesion.setEstado(EstadoBot.SELECCION_SERVICIO);
        return respuesta;
    }

    private String generarMapeoServicios(long IdNegocio) {

        // List<ServiciosModel> servicios =
        // iServicioQuery.obtenerServiciosByNegocio(IdNegocio);
        List<ServiciosModel> servicios = new ArrayList<>();
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
        return mensajeServicio.toString();
    }

    private NegocioModel selecionarNegocio(int opcionValidate, List<NegocioModel> sucursales) {
        return sucursales.get(opcionValidate - 1);
    }

}
