package com.app.citas.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Entity.Usuario;
import com.app.citas.Estados.BotState;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Mapper.ConstrucionMensaje;
import com.app.citas.Mapper.Model.NegocioModel;
import com.app.citas.Services.negocio.INegocioQuery;
import com.app.citas.Services.usuario.EmpleadoQuery;

@Component
public class SucursalState implements BotState {

    @Autowired
    private INegocioQuery negocioQuery;

    @Autowired
    private EmpleadoQuery empleadoQuery;

    @Autowired
    private BotValidador botValidador;

    @Autowired
    private ConstrucionMensaje construcionMensaje;

    @Override
    public String procesar(String from, String to, String body, SesionWhatsapp sesion) {

        List<NegocioModel> sucursales = negocioQuery.encontrarNegocioByNumero(to);

        if (sucursales.size() == 1) {
            return manejarSucursalUnica(sucursales.get(0), sesion);
        }

        Integer opcion = botValidador.validarOpcion(body, sucursales.size());

        if (opcion == null) {
            List<String> nombres = sucursales.stream()
                    .map(NegocioModel::getNombre)
                    .toList();

            return construcionMensaje.ConstruirLista("Selecciona una sucursal:", nombres);
        }

        NegocioModel negocioSeleccionado = sucursales.get(opcion - 1);
        return manejarSucursalUnica(negocioSeleccionado, sesion);
    }

    private String manejarSucursalUnica(NegocioModel sucursal, SesionWhatsapp sesion) {

        sesion.setSucursalId(sucursal.getIdNegocio());

        List<Usuario> empleados = empleadoQuery.obtenerEmpleadosBySucursal(sucursal.getIdNegocio());
        if (empleados.size() == 1) {
            sesion.setEmpleadoId(empleados.get(0).getId());
            sesion.setEstado(EstadoBot.SELECCION_SERVICIO);
            return null;
        }

        sesion.setEmpleadoId(null);
        sesion.setEstado(EstadoBot.SELECCION_EMPLEADO);
        return generarListaEmpleados(empleados);
    }

    private String generarListaEmpleados(List<Usuario> empleados) {

        StringBuilder mensaje = new StringBuilder("Selecciona un empleado:\n\n");

        for (int i = 0; i < empleados.size(); i++) {
            mensaje.append(i + 1)
                    .append("️⃣ ")
                    .append(empleados.get(i).getNombre())
                    .append("\n");
        }

        return mensaje.toString();
    }

    public boolean validarEmpleados(List<Usuario> empleados) {
        if (empleados.size() == 1) {
            return true;
        }
        return false;
    }
}
