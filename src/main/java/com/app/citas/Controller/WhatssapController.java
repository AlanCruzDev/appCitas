package com.app.citas.Controller;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.citas.Entity.Cliente;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Estados.SesionUsuario;
import com.app.citas.Mapper.Model.NegocioModel;
import com.app.citas.Mapper.Model.ServiciosModel;
import com.app.citas.Services.cita.CitaMutations;
import com.app.citas.Services.cita.ICitaQuery;
import com.app.citas.Services.clientes.ClienteMutations;
import com.app.citas.Services.clientes.ClienteQuery;
import com.app.citas.Services.negocio.INegocioQuery;
import com.app.citas.Services.servicios.IServicioQuery;

@RestController
@RequestMapping("api/whatssap")
public class WhatssapController {

    @Autowired
    private INegocioQuery negocioQuery;

    @Autowired
    private IServicioQuery iServicioQuery;

    @Autowired
    private ICitaQuery citaQuery;
    
    @Autowired
    private CitaMutations citaMutations;

    @Autowired
    private ClienteQuery clienteQuery;

    @Autowired
    private ClienteMutations clienteMutations;
    
    
    private Map<String, EstadoBot> estadoUsuarios = new HashMap<>();
    private Map<String, Long> sucursalUsuario = new HashMap<>();
    private SesionUsuario sessionUsuario;



    @PostMapping(value = "/menu", produces = "application/xml")
    public String recibirMensaje(
            @RequestParam("From") String from,
            @RequestParam("to") String to,
            @RequestParam("Body") String body) {

        String respuesta = "";
        EstadoBot estado = estadoUsuarios.getOrDefault(from, EstadoBot.MENU);
        switch (estado) {
            case MENU:

                // CREAR CITA VAMOS A MODIFICAR AQUI
                if (body.trim().equals("1")) {
                    
                    this.sessionUsuario.setSelectMenu(1);
                    List<NegocioModel> sucursales = this.negocioQuery.encontrarNegocioByNumero(to);

                    StringBuilder mensaje = new StringBuilder();
                    mensaje.append("Selecciona una sucursal:\n\n");
                    int i = 1;
                    for (NegocioModel s : sucursales) {
                        mensaje.append(i)
                                .append("️⃣ ")
                                .append(s.getNombre())
                                .append("\n");
                        i++;
                    }
                    respuesta = mensaje.toString();
                    estadoUsuarios.put(from, EstadoBot.SELECCION_SUCURSAL);
                } else {

                    this.sessionUsuario = new SesionUsuario();
                    Cliente cliente =this.clienteQuery.obtenerClienteByNumero(from);
                    if(Objects.isNull(cliente)){
                        cliente= this.clienteMutations.guardarCliente(from);
                    }
                    
                    this.sessionUsuario.setClienteId(cliente.getId());
                    respuesta = "Hola Bienvenido 👋\n\n" +
                            "1️⃣ Agendar cita\n" +
                            "2️⃣ Cancelar cita\n" +
                            "3️⃣ Ver horarios";
                }
                break;
            case SELECCION_SUCURSAL:

                respuesta = "Elegiste la sucursal número " + body;
                int opcionSucursal = Integer.parseInt(body.trim());
                List<NegocioModel> sucursales = this.negocioQuery.encontrarNegocioByNumero(to);
                NegocioModel negocioSelect = sucursales.get(opcionSucursal - 1);
                this.sessionUsuario.setSucursalId(negocioSelect.getIdNegocio());

                sucursalUsuario.put(from, negocioSelect.getIdNegocio());
                List<ServiciosModel> servicios = this.iServicioQuery
                        .obtenerServiciosByNegocio(negocioSelect.getIdNegocio());

                StringBuilder mensajeServicio = new StringBuilder();
                mensajeServicio.append("Selecciona un servicio:\n\n");

                int i = 1;
                for (ServiciosModel s : servicios) {
                    mensajeServicio.append(i)
                            .append("️⃣ ")
                            .append(s.getNombre())
                            .append("️⃣ ")
                            .append("️Precio:  ")
                            .append(s.getPrecio())
                            .append("\n");
                    i++;
                }
                estadoUsuarios.put(from, EstadoBot.SELECCION_SERVICIO);
                respuesta = mensajeServicio.toString();
                break;

            // FALTA SELECCIONAR EL BARBERO QUE QUIERE CORTAR



            case SELECCION_SERVICIO:
                int opcionServicio = Integer.parseInt(body.trim());
                List<ServiciosModel> servicioslist = this.iServicioQuery
                        .obtenerServiciosByNegocio(this.sessionUsuario.getSucursalId());
                ServiciosModel servicioSelect = servicioslist.get(opcionServicio - 1);
                this.sessionUsuario.setServicioId(servicioSelect.getIdServicio());
                List<LocalTime> horarios = this.citaQuery.obtenerHorariosDisponibles(sessionUsuario);
                respuesta = "Elegiste el servicio número " + opcionServicio +"";

                mensajeServicio = new StringBuilder("Selecciona un Horario Disponible:\n\n");
                i = 1;
                for (LocalTime s : horarios) {
                    mensajeServicio.append(i)
                            .append("️⃣ ")
                            .append(s)
                            .append("\n");
                    i++;
                }
                estadoUsuarios.put(from, EstadoBot.SELECCION_SERVICIO);
                respuesta = mensajeServicio.toString();

                estadoUsuarios.put(from, EstadoBot.SELECCION_FECHA);
                break;
            case SELECCION_FECHA:
                int opcionFecha = Integer.parseInt(body.trim());
                List<LocalTime> horariosLista = this.citaQuery.obtenerHorariosDisponibles(sessionUsuario);
                LocalTime horaSelect = horariosLista.get(opcionFecha - 1);
                respuesta=this.citaMutations.guardarCita(sessionUsuario, horaSelect);
                break;
            default:
                break;
        }
        return "<Response><Message>" + respuesta + "</Message></Response>";
    }

}
