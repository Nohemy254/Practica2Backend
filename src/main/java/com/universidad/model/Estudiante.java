package com.universidad.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @Column(nullable = false)
    private String cedula;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false)
    private String carrera;

    @Column(nullable = false)
    private int semestre;

    @Column(nullable = false)
    private String estado = "ACTIVO";

    @Column(nullable = false)
    private LocalDate fechaRegistro;

    // 🚀 NUEVOS CAMPOS QUE NECESITAS para que tu ServiceImpl funcione:
    @Column(unique = true)
    private String numeroInscripcion;

    private String usuarioAlta;
    private LocalDate fechaAlta;

    private String usuarioModificacion;
    private LocalDate fechaModificacion;

    private String usuarioBaja;
    private LocalDate fechaBaja;
    private String motivoBaja;

    @ManyToMany
    private List<Materia> materias;

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDate.now();
        if (estado == null) {
            estado = "ACTIVO";
        }
    }
}
