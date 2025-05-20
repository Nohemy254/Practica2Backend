package com.universidad.repository;

import com.universidad.model.Materia;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface MateriaRepository extends JpaRepository<Materia, Long> {
    Optional<Materia> findByCodigoUnico(String codigoUnico);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Materia> findById(Long id);
}

