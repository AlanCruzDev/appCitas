package com.app.citas.Entity;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuario", schema = "cit_mex")
@Getter
@Setter
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String password;

    @Enumerated(EnumType.STRING)
    private Roles rol;
    private boolean recibeCitas;
    private String telefono;
    private LocalTime hora_inicio;
    private LocalTime hora_cierre;
    private boolean activo;

    // A qué negocio pertenece (si es empleado o admin operativo)
    @ManyToOne
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    // Si es dueño, sus sucursales
    @OneToMany(mappedBy = "dueno")
    private List<Negocio> negocios;

    // Citas donde él atiende
    @OneToMany(mappedBy = "empleado")
    private List<Cita> citas;

    // tabla intermedia
    @ManyToMany
    @JoinTable(name = "usuario_servicio", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "servicio_id"))
    private List<Servicio> servicios;

}
