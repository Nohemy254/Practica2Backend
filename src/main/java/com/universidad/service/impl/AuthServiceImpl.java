package com.universidad.service.impl;

import com.universidad.dto.LoginRequest;
import com.universidad.dto.RegisterRequest;
import com.universidad.dto.JwtResponse;
import com.universidad.model.Rol;
import com.universidad.model.Usuario;
import com.universidad.repository.RolRepository;
import com.universidad.repository.UsuarioRepository;
import com.universidad.security.JwtUtil;
import com.universidad.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;



import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authManager;
    private final UsuarioRepository usuarioRepo;
    private final RolRepository rolRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public JwtResponse login(LoginRequest request) {
        Authentication auth = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        authManager.authenticate(auth);
        String token = jwtUtil.generateToken(request.getUsername());
        return new JwtResponse(token, "Bearer");
    }

    @Override
    public void register(RegisterRequest request) {
        if (usuarioRepo.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Nombre de usuario ya en uso");
        }
        if (usuarioRepo.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Correo electrónico ya en uso");
        }

        Usuario user = new Usuario();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNombre(request.getNombre());
        user.setApellido(request.getApellido());
        user.setActivo(true);
        Rol rolUser = rolRepo.findByNombre(Rol.NombreRol.ROL_ESTUDIANTE)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        user.setRoles(Collections.singleton(rolUser));
        usuarioRepo.save(user);
    }
}
