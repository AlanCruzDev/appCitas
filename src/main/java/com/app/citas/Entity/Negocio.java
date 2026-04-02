package com.app.citas.Entity;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "negocio", schema = "cit_mex")
@Getter
@Setter
public class Negocio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idNegocio;
    private String nombre;
    private String direccion;
    private String telefono;
    private LocalTime hora_apertura;
    private LocalTime hora_cierre;
    private boolean activo;

    // Dueño
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dueno_id", nullable = false)
    private Usuario dueno;

    // Empleados
    @OneToMany(mappedBy = "negocio")
    private List<Usuario> empleados;

    @OneToMany(mappedBy = "negocio")
    private List<Cita> citas;

}
