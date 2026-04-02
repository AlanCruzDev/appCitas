package com.app.citas.Services.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.citas.Entity.Cliente;
import com.app.citas.Entity.Usuario;
import com.app.citas.Mapper.ApiResponse;
import com.app.citas.Repository.ClienteRepository;
import com.app.citas.Repository.UsuarioRepository;
import com.app.citas.Services.clientes.ClienteQuery;
import com.app.citas.Services.usuario.IUsuariosQuery;

@Service
public class LoginMutationImpl implements ILoginMutation {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteQuery clienteQuery;

    @Autowired
    private IUsuariosQuery iUsuariosQuery;

    @Override
    public ApiResponse<?> guardarContrasenia(String contrasenia, String tel) {
        Cliente cliente = clienteQuery.obtenerClienteByNumero(tel);
        if (cliente != null) {
            cliente.setContrasenia(contrasenia);
            this.clienteRepository.save(cliente);
            return new ApiResponse<>(true, "Contrasenia Guardada Correctamente", null);
        }
        Usuario usuario = iUsuariosQuery.obtenerUsuarioByTelefono(tel);
        if (usuario != null) {
            usuario.setPassword(tel);
            this.usuarioRepository.save(usuario);
            return new ApiResponse<>(false, "Contrasenia Guardada Correctamente", null);
        }
        return new ApiResponse<>(false, "Error al Guardada Correctamente", null);
    }

}
