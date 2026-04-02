package com.app.citas.Services.servicios;

import java.util.List;

import com.app.citas.Entity.Servicio;
import com.app.citas.Mapper.Model.ServiciosModel;

public interface IServicioQuery {

    public List<ServiciosModel> obtenerServiciosByNegocio(Long id, Long idUsuario);
    public Servicio findByServicio(Long servicioId);
    
}
