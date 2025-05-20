package com.universidad.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.*;

/**
 * DTO que representa los datos de un estudiante para transferencia entre capas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstudianteDTO implements Serializable {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 3, max = 50, message = "El apellido debe tener entre 3 y 50 caracteres")
    private String apellido;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email no es válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El número de inscripción es obligatorio")
    @Size(min = 5, max = 20, message = "El número de inscripción debe tener entre 5 y 20 caracteres")
    private String numeroInscripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Size(min = 3, max = 20, message = "El estado debe tener entre 3 y 20 caracteres")
    @Pattern(regexp = "^(activo|inactivo)$", message = "El estado debe ser 'activo' o 'inactivo'")
    private String estado;

    @NotBlank(message = "El usuario de alta es obligatorio")
    @Size(min = 3, max = 50, message = "El usuario de alta debe tener entre 3 y 50 caracteres")
    private String usuarioAlta;

    @NotNull(message = "La fecha de alta es obligatoria")
    @PastOrPresent(message = "La fecha de alta debe ser anterior o igual a la fecha actual")
    private LocalDate fechaAlta;

    @Size(min = 3, max = 50, message = "El usuario de modificacion debe tener entre 3 y 50 caracteres")
    private String usuarioModificacion;

    @FutureOrPresent(message = "La fecha de modificacion debe ser mayor o igual a la fecha actual")
    private LocalDate fechaModificacion;

    @Size(min = 3, max = 50, message = "El usuario de baja debe tener entre 3 y 50 caracteres")
    private String usuarioBaja;

    @FutureOrPresent(message = "La fecha de baja debe ser mayor o igual a la fecha actual")
    private LocalDate fechaBaja;

    @Size(min = 3, max = 100, message = "El motivo de baja debe tener entre 3 y 100 caracteres")
    @Pattern(regexp = "^(renuncia|desercion|traslado)$", message = "El motivo de baja debe ser 'renuncia', 'desercion' o 'traslado'")
    private String motivoBaja;

    // 🆕 Campos faltantes que sí existen en la entidad
    @NotBlank(message = "La cédula es obligatoria")
    private String cedula;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "La carrera es obligatoria")
    private String carrera;

    @Min(value = 1, message = "El semestre debe ser mayor o igual a 1")
    private int semestre;

    // Opcional: lista de materias si decides trabajar con ellas
    private List<Long> materiasIds;
}
