package com.universidad.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private String nombre;
    private String apellido;
}
