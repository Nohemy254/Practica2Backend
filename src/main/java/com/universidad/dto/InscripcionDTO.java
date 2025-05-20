// dto/InscripcionDTO.java
package com.universidad.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class InscripcionDTO {
    private Long id;

    @NotNull
    private Long estudianteId;

    @NotNull
    private Long materiaId;

    @NotNull
    private LocalDate fechaInscripcion;
}
