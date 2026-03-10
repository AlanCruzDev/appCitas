package com.app.citas.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.citas.Mapper.ApiResponse;
import com.app.citas.Mapper.UsuarioDto;
import com.app.citas.Services.usuario.UsuarioMutant;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("api/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioMutant usuarioMutant;


    @GetMapping("/saludo")
    public String getSaludo() {
        return new String("HOLA MUNDO");
    }
    

    @PostMapping("/guardar")
    public ResponseEntity<ApiResponse<Object>> guardarEmpleado(@RequestBody  UsuarioDto usuarioDto) {
       this.usuarioMutant.guardarEmpleado(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ApiResponse<>(true, "Usuario creado correctamente", null));
    }
    
    
}
