package com.app.citas.Services.usuario;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.citas.Entity.Negocio;
import com.app.citas.Entity.Usuario;
import com.app.citas.Mapper.UsuarioDto;
import com.app.citas.Repository.UsuarioRepository;
import com.app.citas.Services.negocio.INegocioQuery;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UsuarioMutantImpl implements UsuarioMutant{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private INegocioQuery negocioQuery;

    @Override
    public void guardarEmpleado(UsuarioDto usuarioDto) {
        Usuario user = new Usuario();
        user.setNombre(usuarioDto.getNombre());
        user.setEmail(usuarioDto.getEmail());
        user.setPassword(usuarioDto.getPassword());
        user.setRol(usuarioDto.getRoles());
        user.setActivo(true);
        user.setRecibeCitas(usuarioDto.isRecibeCitas());
        user.setFechaCreacion(LocalDate.now());
        
        Negocio negocio = this.negocioQuery.encontrarNegocioById(usuarioDto.getIdnegocio());
        user.setNegocio(negocio);
        this.usuarioRepository.save(user);
    }
    
}
