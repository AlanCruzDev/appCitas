package com.app.citas.Services.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.citas.Entity.Negocio;
import com.app.citas.Entity.Servicio;
import com.app.citas.Entity.Usuario;
import com.app.citas.Mapper.ServiciosDto;
import com.app.citas.Repository.ServicioRepository;
import com.app.citas.Repository.UsuarioRepository;
import com.app.citas.Services.negocio.INegocioQuery;
import com.app.citas.Services.usuario.EmpleadoQuery;
import com.app.citas.excepciones.NoExisteException;

@Service
public class ServicioMutantImpl implements IServicioMutant {

    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private INegocioQuery negocioQuery;

    @Autowired
    private EmpleadoQuery empleadoQuery;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void desactivar(Long idServicio) {
        Servicio servicio = this.servicioRepository.findById(idServicio).orElse(null);

        if (Objects.nonNull(servicio)) {
            servicio.setActivo(false);
            this.servicioRepository.save(servicio);
        }
    }

    @Override
    public String actualizarServicio(ServiciosDto dto) {

        Usuario user = obtenerUsuario(dto.getIdUsuario());

        Servicio ser = servicioRepository.findById(dto.getIdServicio())
                .orElseThrow(() -> new NoExisteException("Servicio no existe"));

        mapearServicio(ser, dto);

        servicioRepository.save(ser);

        asignarServicioAUsuario(user, ser);

        return "Servicio actualizado correctamente";
    }

    @Override
    public void guardarServicios(List<ServiciosDto> dto) {

        dto.stream().forEach(item -> {
            Usuario user = obtenerUsuario(item.getIdUsuario());
            Servicio ser = new Servicio();
            mapearServicio(ser, item);
            servicioRepository.save(ser);
            asignarServicioAUsuario(user, ser);
        });

    }

    private Usuario obtenerUsuario(Long idUsuario) {
        Usuario user = empleadoQuery.obtenerEmpleadoById(idUsuario);

        if (Objects.isNull(user)) {
            throw new NoExisteException("El usuario no existe");
        }
        return user;
    }

    private void mapearServicio(Servicio ser, ServiciosDto dto) {
        ser.setNombre(dto.getNomServicio());
        ser.setDuracionMinutos(dto.getDuracionMin());
        ser.setInforServicio(dto.getInfoServicio());
        ser.setPrecio(dto.getPrecio());
        ser.setActivo(true);

        Negocio neg = negocioQuery.encontrarNegocioById(dto.getIdNegocio());
        ser.setNegocio(neg);
    }

    private void asignarServicioAUsuario(Usuario user, Servicio ser) {

        if (user.getServicios() == null) {
            user.setServicios(new ArrayList<>());
        }

        boolean existe = user.getServicios()
                .stream()
                .anyMatch(s -> s.getIdServicio().equals(ser.getIdServicio()));

        if (!existe) {
            user.getServicios().add(ser);
            usuarioRepository.save(user);
        }
    }
}
