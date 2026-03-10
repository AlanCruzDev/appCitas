package com.app.citas.Services.negocio;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.citas.Entity.Negocio;
import com.app.citas.Entity.Usuario;
import com.app.citas.Mapper.NegocioDto;
import com.app.citas.Repository.NegocioRepository;
import com.app.citas.excepciones.NoExisteException;

import jakarta.transaction.Transactional;



@Service
@Transactional
public class NegocioMutationImpl implements INegocioMutation{

    @Autowired
    private NegocioRepository negocioRepository;

    @Autowired
    private INegocioQuery iNegocioQuery;


    @Override
    public void guardarNegocioNuevoDuenio(NegocioDto negocioDto) {
        Negocio negocio = new Negocio();
        negocio.setNombre(negocioDto.getNombre());
        negocio.setDireccion(negocioDto.getDireccion());
        negocio.setTelefono(negocioDto.getTelefono());
        negocio.setHora_apertura(negocioDto.getHora_apertura());
        negocio.setHora_cierre(negocioDto.getHora_cierre());
        negocio.setActivo(true);
        Usuario user = this.iNegocioQuery.encontrarDuenosById(negocioDto.getIdUsuario());
        if(Objects.isNull(user)){
            throw new NoExisteException("El usuario no existe");
        }
        
        negocio.setDueno(user);
        this.negocioRepository.save(negocio);   
    }
}
