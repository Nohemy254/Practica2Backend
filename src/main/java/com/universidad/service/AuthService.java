package com.universidad.service;

import com.universidad.dto.LoginRequest;
import com.universidad.dto.RegisterRequest;
import com.universidad.dto.JwtResponse;

public interface AuthService {
    JwtResponse login(LoginRequest request);
    void register(RegisterRequest request);
}
