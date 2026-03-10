package com.app.citas.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.citas.Mapper.ApiResponse;
import com.app.citas.Mapper.NegocioDto;
import com.app.citas.Services.negocio.INegocioMutation;



@RestController
@RequestMapping("api/negocio")
public class NegocioController {

    @Autowired
    private INegocioMutation iNegocioMutation;

    
    @PostMapping("/guardar")
    public ResponseEntity<ApiResponse<Void>> guardarDuenio(@RequestBody NegocioDto negocioDto) {
        this.iNegocioMutation.guardarNegocioNuevoDuenio(negocioDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Negocio guardado correctamente", null));
    }
    
}
