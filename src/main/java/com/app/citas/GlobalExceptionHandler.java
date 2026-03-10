package com.app.citas;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.app.citas.Mapper.ApiResponse;
import com.app.citas.excepciones.NoExisteException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    

    @ExceptionHandler(NoExisteException.class)
    public ResponseEntity<ApiResponse<Void>> manejarSinExistencia(NoExisteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(false, ex.getMessage(), null));
    }

   
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> manejarErrorGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Error interno del servidor", ex.getMessage()));
    }
}
