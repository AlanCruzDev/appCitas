package com.app.citas.Services.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.citas.Entity.Cliente;
import com.app.citas.Entity.Usuario;
import com.app.citas.Mapper.ApiResponse;
import com.app.citas.Mapper.Model.LoginResponse;
import com.app.citas.Services.clientes.ClienteQuery;
import com.app.citas.Services.usuario.IUsuariosQuery;

@Service
public class LoginQueryImpl implements ILoginQuery {

    @Autowired
    private ClienteQuery clienteQuery;

    @Autowired
    private IUsuariosQuery iUsuariosQuery;

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<?> logearClienteOrEmpleado(String numero, String password) {

        LoginResponse response = new LoginResponse();

        Cliente cliente = clienteQuery.obtenerClienteByNumero(numero);

        if (cliente != null) {
            response.setId(cliente.getId());
            response.setNumero(cliente.getTelefono());
            response.setMenu("cliente");
            return validarLogin(cliente.getContrasenia(), password, response);
        }

        Usuario usuario = iUsuariosQuery.obtenerUsuarioByTelefono(numero);

        if (usuario != null) {
            response.setId(usuario.getId());
            response.setNumero(usuario.getTelefono());
            response.setMenu("usuario");
            response.setRol(usuario.getRol().name());
            return validarLogin(usuario.getPassword(), password, response);
        }

        return new ApiResponse<>(false, "Usuario no encontrado", null);
    }

    private ApiResponse<?> validarLogin(String passDB, String passInput, Object data) {
        if (passInput.equals(passDB)) {
            return new ApiResponse<>(true, "Exito", data);
        }
        return new ApiResponse<>(false, "Error de Contraseña u Usuario", null);
    }
}
