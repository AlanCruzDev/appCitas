package com.app.citas.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.citas.Mapper.body.ClientesBody;
import com.app.citas.Services.login.ILoginMutation;
import com.app.citas.Services.login.ILoginQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private ILoginQuery iLoginService;

    @Autowired
    private ILoginMutation loginMutation;

    @GetMapping("/authentication/{tel}/{pass}")
    public ResponseEntity<?> getMethodName(@PathVariable("tel") String tel, @PathVariable("pass") String pass) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.iLoginService.logearClienteOrEmpleado(tel, pass));
    }

    @PostMapping("/crear")
    public ResponseEntity<?> postMethodName(@RequestBody ClientesBody clientesBody) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.loginMutation.guardarContrasenia(clientesBody.getContrasenia(), clientesBody.getTel()));
    }

}
