
package com.app.citas.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.citas.Mapper.Model.CitalModel;
import com.app.citas.Mapper.body.CitaBody;
import com.app.citas.Services.cita.CitaMutations;
import com.app.citas.Services.cita.ICitaQuery;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/citas")
public class CitasController {

    @Autowired
    private ICitaQuery citaQuery;

    @Autowired
    private CitaMutations citaMutations;

    @GetMapping("/del/dia/{idNegocio}/{idEmpleado}")
    public ResponseEntity<List<CitalModel>> getMethodName(@PathVariable("idNegocio") Long idNegocio,
            @PathVariable("idEmpleado") Long idEmpleado) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.citaQuery.ObtenerCitasPendientes(idEmpleado, idNegocio));
    }

    @PostMapping("/crear/rapido")
    public ResponseEntity<String> CitaRapida(@RequestBody CitaBody citaBody) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.citaMutations.guardarCitaRapida(citaBody));
    }

}
