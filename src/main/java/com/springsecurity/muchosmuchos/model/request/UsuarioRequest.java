package com.springsecurity.muchosmuchos.model.request;

import java.util.List;

import lombok.Data;

@Data
public class UsuarioRequest {
    
    private String email;
    private String contrasenia;
    private List<String> roles;
}
