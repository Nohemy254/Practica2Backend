package com.universidad.repository;

import com.universidad.model.Estudiante;

import jakarta.persistence.LockModeType;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    boolean existsByUsuarioEmail(String email);

    Boolean existsByNumeroInscripcion(String numeroInscripcion); // Método para verificar si existe un estudiante por su número de inscripción

    // Método para encontrar un estudiante por su número de inscripción
    Estudiante findByNumeroInscripcion(String numeroInscripcion); 

    // Método para encontrar un estudiante por su estado
    Estudiante findByEstado(String estado); // Método para encontrar un estudiante por su estado

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Estudiante> findById(Long id);
}
