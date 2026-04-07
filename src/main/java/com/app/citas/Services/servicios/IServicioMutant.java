package com.app.citas.Services.servicios;

import java.util.List;

import com.app.citas.Mapper.ServiciosDto;

public interface IServicioMutant {

    public void guardarServicios(List<ServiciosDto> serviciosDto);

    public String actualizarServicio(ServiciosDto serviciosDto);

    public void desactivar(Long idServicio);
}
