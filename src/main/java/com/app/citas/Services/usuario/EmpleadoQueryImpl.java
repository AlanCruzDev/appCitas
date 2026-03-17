package com.app.citas.Services.usuario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.Usuario;
import com.app.citas.Repository.UsuarioRepository;

@Service
@Transactional(readOnly = true)
public class EmpleadoQueryImpl  implements EmpleadoQuery{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public Usuario obtenerEmpleadoById(Long idUsuario) {
        return this.usuarioRepository.empleadoById(idUsuario);
    }

    
    @Override
    public List<Usuario> obtenerEmpleadosBySucursal(Long idSucursal) {
        List<Usuario> usuarios= this.usuarioRepository.encontrarEmpleadosBySucursal(idSucursal);
        return usuarios;
    }       
}
