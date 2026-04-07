package com.app.citas.Entity;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "servicio", schema = "cit_mex")
@Getter
@Setter
public class Servicio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idServicio;
    private String nombre;

    @Column(name = "duracion_minutos")
    private int duracionMinutos;
    private float precio;
    private boolean activo;
    private String inforServicio;

    @ManyToOne
    @JoinColumn(name = "negocio_id", nullable = false)
    private Negocio negocio;

    @OneToMany(mappedBy = "servicio")
    private List<Cita> citas;

    // tabla intermedia
    @ManyToMany(mappedBy = "servicios")
    private List<Usuario> usuarios;

}
