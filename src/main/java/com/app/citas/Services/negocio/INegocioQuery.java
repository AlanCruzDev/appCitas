package com.app.citas.Services.negocio;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.app.citas.Entity.Negocio;
import com.app.citas.Entity.Usuario;
import com.app.citas.Mapper.Model.NegocioModel;

public interface INegocioQuery{
    
    public Usuario encontrarDuenosById(@Param("id") Long id);
    public Negocio encontrarNegocioById(@Param("id") Long id);
    public List<NegocioModel> encontrarNegocioByNumero(@Param("numero") String numero);
}
