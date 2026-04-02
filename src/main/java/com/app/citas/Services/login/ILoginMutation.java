package com.app.citas.Services.login;

import com.app.citas.Mapper.ApiResponse;

public interface ILoginMutation {

    public ApiResponse<?> guardarContrasenia(String contrasenia, String tel);

}
