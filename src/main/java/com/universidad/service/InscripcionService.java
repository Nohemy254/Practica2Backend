package com.universidad.service;

import com.universidad.dto.InscripcionDTO;
import com.universidad.model.Estudiante;
import com.universidad.model.Inscripcion;
import com.universidad.model.Materia;
import com.universidad.repository.EstudianteRepository;
import com.universidad.repository.InscripcionRepository;
import com.universidad.repository.MateriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepo;

    @Autowired
    private EstudianteRepository estudianteRepo;

    @Autowired
    private MateriaRepository materiaRepo;

    public List<Inscripcion> listar() {
        return inscripcionRepo.findAll();
    }

    public Inscripcion crear(InscripcionDTO dto) {
        Estudiante estudiante = estudianteRepo.findById(dto.getEstudianteId())
                .orElseThrow(() -> new NoSuchElementException("Estudiante no encontrado"));

        Materia materia = materiaRepo.findById(dto.getMateriaId())
                .orElseThrow(() -> new NoSuchElementException("Materia no encontrada"));

        Inscripcion insc = new Inscripcion();
        insc.setEstudiante(estudiante);
        insc.setMateria(materia);
        insc.setFechaInscripcion(dto.getFechaInscripcion());

        return inscripcionRepo.save(insc);
    }

    public Inscripcion obtener(Long id) {
        return inscripcionRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Inscripción no encontrada"));
    }

    public Inscripcion actualizar(Long id, InscripcionDTO dto) {
        Inscripcion insc = obtener(id);
        insc.setFechaInscripcion(dto.getFechaInscripcion());
        return inscripcionRepo.save(insc);
    }

    public void eliminar(Long id) {
        inscripcionRepo.deleteById(id);
    }
}
