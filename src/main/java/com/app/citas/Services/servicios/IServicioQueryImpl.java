package com.app.citas.Services.servicios;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.citas.Entity.Servicio;
import com.app.citas.Mapper.Model.ServiciosModel;
import com.app.citas.Repository.ServicioRepository;

@Service
public class IServicioQueryImpl implements IServicioQuery {

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    public List<ServiciosModel> obtenerServiciosByNegocio(Long id, Long idUsuario) {
        List<Servicio> servicios = this.servicioRepository.obtenerServiciosByNegocioYUsuario(id, idUsuario);

        return servicios.stream().map(item -> {

            ServiciosModel serModel = new ServiciosModel();
            serModel.setIdServicio(item.getIdServicio());
            serModel.setNomServicio(item.getNombre());
            serModel.setDuracionMin(item.getDuracionMinutos());
            serModel.setPrecio(item.getPrecio());
            serModel.setInfoServicio(item.getInforServicio());

            /*
             * List<UsuarioModel> user = item.getUsuarios().stream().map(u -> {
             * UsuarioModel usuario = new UsuarioModel();
             * usuario.setId(u.getId());
             * usuario.setNombre(u.getNombre());
             * return usuario;
             * }).collect(Collectors.toList());
             * serModel.setUsuarioModels(user);
             */
            return serModel;
        }).collect(Collectors.toList());
    }

    @Override
    public Servicio findByServicio(Long servicioId) {
        if (servicioId != null)
            return this.servicioRepository.findById(servicioId).get();
        return null;
    }

}
