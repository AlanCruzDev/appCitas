package com.app.citas.Services.login;

import com.app.citas.Mapper.ApiResponse;

public interface ILoginQuery {

    public ApiResponse<?> logearClienteOrEmpleado(String numero, String password);
}
