package com.springsecurity.muchosmuchos.services.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.springsecurity.muchosmuchos.model.entity.UsuarioEntity;

public interface UsuarioInterface extends UserDetailsService{
    
    public void guardarUsuario(UsuarioEntity usuarioEntity, List<String> rolesNombres);
}
