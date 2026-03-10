package com.app.citas.Services.servicios;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.citas.Entity.Servicio;
import com.app.citas.Mapper.Model.ServiciosModel;
import com.app.citas.Repository.ServicioRepository;

@Service
public class IServicioQueryImpl implements IServicioQuery{

    @Autowired
    private ServicioRepository servicioRepository;

    @Override
    public List<ServiciosModel> obtenerServiciosByNegocio(Long id) {
        List<Servicio> servicios = this.servicioRepository.ObtenerServiciosByNegocio(id);
        return servicios.stream().map(item ->{

            ServiciosModel serModel = new ServiciosModel();
            serModel.setIdServicio(item.getIdServicio());
            serModel.setNombre(item.getNombre());
            serModel.setDuracion_minutos(item.getDuracionMinutos());
            serModel.setPrecio(item.getPrecio());
            return serModel;
        }).collect(Collectors.toList());
    }

    @Override
    public Servicio findByServicio(Long servicioId) {
        return servicioRepository.findById(servicioId).get();
    }

    
}
