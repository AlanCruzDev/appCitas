package com.app.citas.Entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.app.citas.Estados.EstadoBot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "SesionWhatsapp", schema = "cit_mex")
@Getter
@Setter
public class SesionWhatsapp implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idsesion")
    private Long id;

    @Column(unique = true)
    private String telefono;

    @Enumerated(EnumType.STRING)
    private EstadoBot estado;

    private Long clienteId;
    private Long sucursalId;
    private Long servicioId;
    private Long empleadoId;

    private LocalDate fechaCreacion;
    private LocalTime hora;
    private LocalDateTime ultimaInteraccion;

    @Transient
    private String mensajeSistema;

    @PrePersist
    public void prePersist() {
        this.ultimaInteraccion = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.ultimaInteraccion = LocalDateTime.now();
    }

}
