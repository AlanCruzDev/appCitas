package com.app.citas.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.app.citas.Mapper.ApiResponse;
import com.app.citas.Mapper.ServiciosDto;
import com.app.citas.Mapper.Model.ServiciosModel;
import com.app.citas.Services.servicios.IServicioMutant;
import com.app.citas.Services.servicios.IServicioQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/servicio")
public class ServicioController {

    @Autowired
    private IServicioQuery iServicioQuery;
    @Autowired
    private IServicioMutant iServicioMutant;

    @GetMapping("/{idnegocio}/{idUsuario}")
    public ResponseEntity<List<ServiciosModel>> getMethodName(
            @PathVariable("idnegocio") Long idnegocio, @PathVariable("idUsuario") Long idUsuario) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.iServicioQuery.obtenerServiciosByNegocio(idnegocio, idUsuario));

    }

    @PostMapping("/guardar")
    public ResponseEntity<ApiResponse<Void>> guardarServicios(@RequestBody ServiciosDto serviciosDto) {
        this.iServicioMutant.guardarServicios(serviciosDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Servicio creado correctamente", null));

    }

    @PutMapping("/actualizar")
    public ResponseEntity<ApiResponse<Void>> actualizarServicios(@RequestBody ServiciosDto serviciosDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, this.iServicioMutant.actualizarServicio(serviciosDto), null));

    }

}
