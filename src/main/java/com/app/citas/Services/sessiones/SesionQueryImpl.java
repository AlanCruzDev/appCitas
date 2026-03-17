package com.app.citas.Services.sessiones;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.SesionWhatsapp;
import com.app.citas.Repository.SesionWhatsappRepository;

@Service
@Transactional
public class SesionQueryImpl implements ISesionQuery{


    @Autowired
    private SesionWhatsappRepository sesionWhatsappRepository;

    @Autowired
    private ISesionMutant sesionMutant;



    private static final int MINUTOS_EXPIRACION=15;




    @Override
    public SesionWhatsapp obtenerSesion(String telefono) {
        SesionWhatsapp sesion = this.sesionWhatsappRepository.findByTelefono(telefono).orElse(null);        
        if(sesion == null){
            sesion = this.sesionMutant.crearSesionNueva(telefono);
            return sesion;
        }

        if(sesionExpirada(sesion)){
            sesion=this.sesionMutant.reiniciarSesion(sesion);
            sesion.setMensajeSistema("Tu sesión expiró, iniciemos nuevamente.");
        }
        return sesion;
    }

    @Override
    public boolean sesionExpirada(SesionWhatsapp sesion) {
        LocalDateTime limite = LocalDateTime.now().minusMinutes(MINUTOS_EXPIRACION);
        return sesion.getUltimaInteraccion().isBefore(limite);
    }
}
