package com.app.citas.Services.usuario;

import java.util.List;

import com.app.citas.Entity.Usuario;

public interface EmpleadoQuery {
    public List<Usuario> obtenerEmpleadosBySucursal(Long idSucursal);

    Usuario obtenerEmpleadoById(Long idUsuario);
}
