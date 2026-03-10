package com.app.citas.Services.negocio;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.Negocio;
import com.app.citas.Entity.Usuario;
import com.app.citas.Mapper.Model.NegocioModel;
import com.app.citas.Repository.NegocioRepository;
import com.app.citas.Repository.UsuarioRepository;

@Service
@Transactional(readOnly = true)
public class NegocioQueryImpl implements INegocioQuery{

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private NegocioRepository negocioRepository;

    @Override
    public Usuario encontrarDuenosById(Long id) {
        return this.usuarioRepository.encontrarDuenosById(id);
    }

    @Override
    public Negocio encontrarNegocioById(Long id) {
        return this.negocioRepository.encontrarNegocioById(id);
    }


    @Override
    public List<NegocioModel> encontrarNegocioByNumero(String numero) {

    List<Negocio> nego =this.negocioRepository.encontrarNegocioByNumero(numero);

    return nego.stream().map(item ->{
        NegocioModel negocioModel = new NegocioModel();
        negocioModel.setNombre(item.getNombre());
        negocioModel.setIdNegocio(item.getIdNegocio());
        negocioModel.setHora_cierre(item.getHora_cierre());
        return negocioModel;
    }).collect(Collectors.toList());
    }
    
}
