package com.app.citas.Services.cita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.Cita;
import com.app.citas.Entity.Servicio;
import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Entity.Usuario;
import com.app.citas.Mapper.Model.CitalModel;
import com.app.citas.Mapper.Model.ClienteModel;
import com.app.citas.Mapper.Model.ServiciosModel;
import com.app.citas.Repository.CitaRepository;
import com.app.citas.Services.clientes.ClienteQuery;
import com.app.citas.Services.servicios.IServicioQuery;
import com.app.citas.Services.usuario.EmpleadoQuery;

@Service
public class CitaQueryImpl implements ICitaQuery {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private IServicioQuery iServicioQuery;

    @Autowired
    private ClienteQuery clienteQuery;

    @Autowired
    private EmpleadoQuery empleadoQuery;

    @Transactional(readOnly = true)
    @Override
    public List<CitalModel> ObtenerCitasPendientes(Long idCliente, Long idNegocio) {
        List<Cita> citaslist = this.citaRepository.obtenerCitasDelDia(idNegocio, LocalDate.now(), idCliente);
        return citaslist.stream().map(item -> {
            CitalModel citas = new CitalModel();

            citas.setId(item.getId());
            citas.setFecha(item.getFecha());
            citas.setHoraInicio(item.getHoraInicio());
            citas.setHoraFin(item.getHoraFin());
            citas.setEstado(item.getEstado());

            if (item.getCliente() != null) {
                ClienteModel clienteModel = new ClienteModel();
                clienteModel.setId(item.getCliente().getId());
                clienteModel.setNombre(item.getCliente().getNombre());
                citas.setCliente(clienteModel);
            }

            ServiciosModel serviciosModel = new ServiciosModel();
            serviciosModel.setIdServicio(item.getServicio().getIdServicio());
            serviciosModel.setPrecio(item.getServicio().getPrecio());
            serviciosModel.setNomServicio(item.getServicio().getNombre());

            citas.setServicio(serviciosModel);

            return citas;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Cita> verficarCantidadCitasAgendadas(String telefono, LocalDate fecha) {
        Long clienteId = this.clienteQuery.obtenerClienteByNumero(telefono).getId();
        List<Cita> cantidad = this.citaRepository.obtenerCitasDelDia(clienteId, fecha);
        return cantidad;
    }

    @Override
    public LocalDate buscarSiguienteDiaDisponible(SesionWhatsapp sesion) {
        LocalDate fecha = sesion.getFechaCreacion();
        for (int i = 1; i <= 7; i++) {
            LocalDate nuevaFecha = fecha.plusDays(i);
            sesion.setFechaCreacion(nuevaFecha);
            List<LocalTime> horarios = procesarHoraUsuario(sesion);

            if (!horarios.isEmpty()) {
                return nuevaFecha;
            }

        }
        return null;
    }

    @Override
    public List<LocalTime> procesarHoraUsuario(SesionWhatsapp sesion) {

        List<LocalTime> disponibles = obtenerHorariosDisponibles(sesion);
        return disponibles;
    }

    @Override
    public List<LocalTime> obtenerHorariosDisponibles(SesionWhatsapp sesion) {

        List<Cita> citas = citaRepository.obtenerCitasDelDia(
                sesion.getSucursalId(),
                sesion.getFechaCreacion(),
                sesion.getEmpleadoId());

        Servicio servicio = this.iServicioQuery.findByServicio(sesion.getServicioId());
        Usuario empleado = this.empleadoQuery.obtenerEmpleadoById(sesion.getEmpleadoId());

        LocalTime inicio = empleado.getHora_inicio();
        LocalTime fin = normalizarHoraCierre(empleado.getHora_cierre());

        if (inicio == null || fin == null || !inicio.isBefore(fin)) {
            throw new RuntimeException("Horario inválido del empleado");
        }

        return generarBloques(inicio, fin, servicio.getDuracionMinutos(), citas);
    }

    private LocalTime normalizarHoraCierre(LocalTime cierre) {
        if (cierre.equals(LocalTime.MIDNIGHT)) {
            return LocalTime.of(23, 59);
        }
        return cierre;
    }

    private List<LocalTime> generarBloques(
            LocalTime inicio,
            LocalTime fin,
            int duracionServicio,
            List<Cita> citasDelDia) {

        List<LocalTime> disponibles = new ArrayList<>();

        LocalTime horaActual = inicio;

        int contador = 0;
        int LIMITE = 48;

        while ((horaActual.plusMinutes(duracionServicio).isBefore(fin)
                || horaActual.plusMinutes(duracionServicio).equals(fin))
                && contador < LIMITE) {

            contador++;

            LocalTime horaFin = horaActual.plusMinutes(duracionServicio);

            boolean ocupado = false;

            for (Cita cita : citasDelDia) {
                if (horaActual.isBefore(cita.getHoraFin()) &&
                        horaFin.isAfter(cita.getHoraInicio())) {

                    ocupado = true;
                    break;
                }
            }

            if (!ocupado) {
                disponibles.add(horaActual);
            }

            horaActual = horaActual.plusMinutes(duracionServicio);
        }
        return disponibles;
    }

    public String formatearHora(LocalTime hora) {
        return hora.format(java.time.format.DateTimeFormatter.ofPattern("h:mm a"));
    }
}
