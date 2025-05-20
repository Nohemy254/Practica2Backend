package com.universidad.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class MateriaDTO {
    private Long id;

    @NotBlank
    private String nombreMateria;

    @NotBlank
    private String codigoUnico;

    @NotNull
    private Integer creditos;

    private Long docenteId;


    private List<Long> prerequisitos;

    /**
     * Lista de IDs de materias para las que esta materia es prerequisito.
     */
    private List<Long> esPrerequisitoDe;
}