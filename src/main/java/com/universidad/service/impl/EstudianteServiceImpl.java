package com.universidad.service.impl;

import com.universidad.dto.EstudianteDTO;
import com.universidad.model.Estudiante;
import com.universidad.model.Materia;
import com.universidad.model.Usuario;
import com.universidad.repository.EstudianteRepository;
import com.universidad.service.IEstudianteService;
import com.universidad.validation.EstudianteValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstudianteServiceImpl implements IEstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private EstudianteValidator estudianteValidator;

    public EstudianteServiceImpl(EstudianteRepository estudianteRepository, EstudianteValidator estudianteValidator) {
        this.estudianteRepository = estudianteRepository;
        this.estudianteValidator = estudianteValidator;
    }

    @Override
    @Cacheable(value = "estudiantes")
    public List<EstudianteDTO> obtenerTodosLosEstudiantes() {
        return estudianteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "estudiante", key = "#numeroInscripcion")
    public EstudianteDTO obtenerEstudiantePorNumeroInscripcion(String numeroInscripcion) {
        Estudiante estudiante = estudianteRepository.findByNumeroInscripcion(numeroInscripcion);
        return convertToDTO(estudiante);
    }

    @Override
    @Cacheable(value = "estudiantesActivos")
    public List<EstudianteDTO> obtenerEstudianteActivo() {
        return estudianteRepository.findAll().stream()
                .filter(est -> "activo".equalsIgnoreCase(est.getEstado()))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "materiasEstudiante", key = "#estudianteId")
    public List<Materia> obtenerMateriasDeEstudiante(Long estudianteId) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        return estudiante.getMaterias();
    }

    @Override
    @CachePut(value = "estudiante", key = "#result.numeroInscripcion")
    @CacheEvict(value = {"estudiantes", "estudiantesActivos"}, allEntries = true)
    public EstudianteDTO crearEstudiante(EstudianteDTO estudianteDTO) {
        estudianteValidator.validacionCompletaEstudiante(estudianteDTO);
        Estudiante estudiante = convertToEntity(estudianteDTO);
        Estudiante estudianteGuardado = estudianteRepository.save(estudiante);
        return convertToDTO(estudianteGuardado);
    }

    @Override
    @CachePut(value = "estudiante", key = "#id")
    @CacheEvict(value = {"estudiantes", "estudiantesActivos"}, allEntries = true)
    public EstudianteDTO actualizarEstudiante(Long id, EstudianteDTO estudianteDTO) {
        Estudiante estudianteExistente = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        Usuario usuario = estudianteExistente.getUsuario();
        usuario.setNombre(estudianteDTO.getNombre());
        usuario.setApellido(estudianteDTO.getApellido());
        usuario.setEmail(estudianteDTO.getEmail());

        estudianteExistente.setFechaNacimiento(estudianteDTO.getFechaNacimiento());
        estudianteExistente.setNumeroInscripcion(estudianteDTO.getNumeroInscripcion());
        estudianteExistente.setUsuarioModificacion("admin");
        estudianteExistente.setFechaModificacion(LocalDate.now());

        Estudiante estudianteActualizado = estudianteRepository.save(estudianteExistente);
        return convertToDTO(estudianteActualizado);
    }

    @Override
    @CacheEvict(value = {"estudiante", "estudiantes", "estudiantesActivos"}, allEntries = true)
    public EstudianteDTO eliminarEstudiante(Long id, EstudianteDTO estudianteDTO) {
        Estudiante estudianteExistente = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));

        estudianteExistente.setEstado("inactivo");
        estudianteExistente.setUsuarioBaja("admin");
        estudianteExistente.setFechaBaja(LocalDate.now());
        estudianteExistente.setMotivoBaja(estudianteDTO.getMotivoBaja());

        Estudiante estudianteInactivo = estudianteRepository.save(estudianteExistente);
        return convertToDTO(estudianteInactivo);
    }

    @Transactional
    public Estudiante obtenerEstudianteConBloqueo(Long id) {
        Estudiante est = estudianteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estudiante no encontrado"));
        try { Thread.sleep(15000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        return est;
    }

    // DTO -> Entidad
    private Estudiante convertToEntity(EstudianteDTO dto) {
        Usuario usuario = Usuario.builder()
                .nombre(dto.getNombre())
                .apellido(dto.getApellido())
                .email(dto.getEmail())
                .build();

        return Estudiante.builder()
                .id(dto.getId())
                .usuario(usuario)
                .fechaNacimiento(dto.getFechaNacimiento())
                .numeroInscripcion(dto.getNumeroInscripcion())
                .usuarioAlta(dto.getUsuarioAlta())
                .fechaAlta(dto.getFechaAlta())
                .usuarioModificacion(dto.getUsuarioModificacion())
                .fechaModificacion(dto.getFechaModificacion())
                .estado(dto.getEstado())
                .usuarioBaja(dto.getUsuarioBaja())
                .fechaBaja(dto.getFechaBaja())
                .motivoBaja(dto.getMotivoBaja())
                .build();
    }

    // Entidad -> DTO
    private EstudianteDTO convertToDTO(Estudiante est) {
        Usuario usuario = est.getUsuario();

        return EstudianteDTO.builder()
                .id(est.getId())
                .nombre(usuario.getNombre())
                .apellido(usuario.getApellido())
                .email(usuario.getEmail())
                .fechaNacimiento(est.getFechaNacimiento())
                .numeroInscripcion(est.getNumeroInscripcion())
                .estado(est.getEstado())
                .usuarioAlta(est.getUsuarioAlta())
                .fechaAlta(est.getFechaAlta())
                .usuarioModificacion(est.getUsuarioModificacion())
                .fechaModificacion(est.getFechaModificacion())
                .usuarioBaja(est.getUsuarioBaja())
                .fechaBaja(est.getFechaBaja())
                .motivoBaja(est.getMotivoBaja())
                .build();
    }
}
