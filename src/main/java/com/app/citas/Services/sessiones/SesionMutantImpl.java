package com.app.citas.Services.sessiones;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Estados.EstadoBot;
import com.app.citas.Repository.SesionWhatsappRepository;

@Service
@Transactional
public class SesionMutantImpl implements ISesionMutant{


    @Autowired
    private SesionWhatsappRepository sesionWhatsappRepository;



    @Override
    public SesionWhatsapp crearSesionNueva(String telefono) {
        SesionWhatsapp sesion = new SesionWhatsapp();
        sesion.setTelefono(telefono);
        sesion.setEstado(EstadoBot.MENU);
        return this.sesionWhatsappRepository.save(sesion);
    }


    @Override
    public SesionWhatsapp reiniciarSesion(SesionWhatsapp sesion) {

        sesion.setEstado(EstadoBot.MENU);
        sesion.setSucursalId(null);
        sesion.setServicioId(null);
        sesion.setClienteId(null);
        sesion.setEmpleadoId(null);
        sesion.setUltimaInteraccion(LocalDateTime.now());
        return this.sesionWhatsappRepository.save(sesion);
    }


    @Override
    public void guardarSesion(SesionWhatsapp sesion) {
        sesion.setUltimaInteraccion(LocalDateTime.now());
        this.sesionWhatsappRepository.save(sesion);
    }
    
}
