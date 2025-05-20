package com.universidad.service.impl;

import com.universidad.model.Docente;
import com.universidad.model.Materia;
import com.universidad.repository.DocenteRepository;
import com.universidad.repository.MateriaRepository;
import com.universidad.service.IMateriaService;

import jakarta.persistence.EntityNotFoundException;

import com.universidad.dto.MateriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MateriaServiceImpl implements IMateriaService {

    @Autowired
    private MateriaRepository materiaRepository;

    // Método utilitario para mapear Materia a MateriaDTO
    private MateriaDTO mapToDTO(Materia materia) {
    return MateriaDTO.builder()
            .id(materia.getId())
            .nombreMateria(materia.getNombreMateria())
            .codigoUnico(materia.getCodigoUnico())
            .creditos(materia.getCreditos())
            .docenteId(materia.getDocente() != null ? materia.getDocente().getId() : null)
            .prerequisitos(materia.getPrerequisitos() != null ?
                materia.getPrerequisitos().stream().map(Materia::getId).collect(Collectors.toList()) : null)
            .esPrerequisitoDe(materia.getEsPrerequisitoDe() != null ?
                materia.getEsPrerequisitoDe().stream().map(Materia::getId).collect(Collectors.toList()) : null)
            .build();
    }


    @Override
    @Cacheable(value = "materias")
    public List<MateriaDTO> obtenerTodasLasMaterias() {
        return materiaRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "materia", key = "#id")
    public MateriaDTO obtenerMateriaPorId(Long id) {
        return materiaRepository.findById(id).map(this::mapToDTO).orElse(null);
    }

    @Override
    @Cacheable(value = "materia", key = "#codigoUnico")
    public MateriaDTO obtenerMateriaPorCodigoUnico(String codigoUnico) {
        Materia materia = materiaRepository.findByCodigoUnico(codigoUnico)
            .orElseThrow(() -> new EntityNotFoundException("Materia con código " + codigoUnico + " no encontrada"));

        return mapToDTO(materia);
    }

    @Autowired
    private DocenteRepository docenteRepository;

    public MateriaDTO crearMateria(MateriaDTO dto) {
        Materia materia = new Materia();
        materia.setNombreMateria(dto.getNombreMateria());
        materia.setCodigoUnico(dto.getCodigoUnico());
        materia.setCreditos(dto.getCreditos());

        if (dto.getDocenteId() != null) {
            Docente docente = docenteRepository.findById(dto.getDocenteId())
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado"));
            materia.setDocente(docente);
        }

        return mapToDTO(materiaRepository.save(materia));
    }


    @Override
    @CachePut(value = "materia", key = "#id")
    @CacheEvict(value = "materias", allEntries = true)
    public MateriaDTO actualizarMateria(Long id, MateriaDTO materiaDTO) {
        Materia materia = materiaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Materia no encontrada con ID: " + id));

        // Actualizar campos básicos
        materia.setNombreMateria(materiaDTO.getNombreMateria());
        materia.setCodigoUnico(materiaDTO.getCodigoUnico());
        materia.setCreditos(materiaDTO.getCreditos());

        // Validar y actualizar prerequisitos
        if (materiaDTO.getPrerequisitos() != null && !materiaDTO.getPrerequisitos().isEmpty()) {
            List<Materia> prerequisitos = materiaRepository.findAllById(materiaDTO.getPrerequisitos());

            // Verificar si algún prerequisito genera un ciclo
            for (Materia prereq : prerequisitos) {
                if (prereq.formariaCirculo(materia.getId())) {
                    throw new IllegalArgumentException("Agregar el prerequisito '" + prereq.getNombreMateria() + "' generaría un ciclo.");
                }
            }

            materia.setPrerequisitos(prerequisitos);
        } else {
            materia.setPrerequisitos(null);
        }

        Materia updatedMateria = materiaRepository.save(materia);
        return mapToDTO(updatedMateria);
    }


    @Override
    @CacheEvict(value = {"materia", "materias"}, allEntries = true)
    public void eliminarMateria(Long id) {
        materiaRepository.deleteById(id);
    }
}
