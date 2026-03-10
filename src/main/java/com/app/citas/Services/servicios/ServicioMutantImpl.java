package com.app.citas.Services.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.citas.Entity.Negocio;
import com.app.citas.Entity.Servicio;
import com.app.citas.Mapper.ServiciosDto;
import com.app.citas.Repository.ServicioRepository;
import com.app.citas.Services.negocio.INegocioQuery;

@Service
public class ServicioMutantImpl implements IServicioMutant {


    @Autowired
    private ServicioRepository servicioRepository;

    @Autowired
    private INegocioQuery negocioQuery;


    @Override
    public void guardarServicios(ServiciosDto serviciosDto) {

        Servicio ser = new Servicio();
        ser.setNombre(serviciosDto.getNombre());
        ser.setDuracionMinutos(serviciosDto.getDuracion_minutos());
        ser.setPrecio(serviciosDto.getPrecio());
        ser.setActivo(true);
        Negocio neg = this.negocioQuery.encontrarNegocioById(serviciosDto.getIdNegocio());
        ser.setNegocio(neg);
        this.servicioRepository.save(ser);     
    }
    
}
