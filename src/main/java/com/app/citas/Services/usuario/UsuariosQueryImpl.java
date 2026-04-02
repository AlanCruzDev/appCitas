package com.app.citas.Services.usuario;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.Usuario;
import com.app.citas.Repository.UsuarioRepository;

@Service
public class UsuariosQueryImpl implements IUsuariosQuery {

    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    @Override
    public Usuario obtenerUsuarioByTelefono(String telefono) {
        return this.usuarioRepository.usuarioByTelefono(telefono);
    }

}
