package com.springsecurity.muchosmuchos.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springsecurity.muchosmuchos.exception.EmailExistsException;
import com.springsecurity.muchosmuchos.model.entity.UsuarioEntity;
import com.springsecurity.muchosmuchos.model.request.UsuarioRequest;
import com.springsecurity.muchosmuchos.services.implementations.UsuarioService;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/guardarUsuario")
    public ResponseEntity<String> guardarUsuario(@RequestBody UsuarioRequest usuarioRequest) {
        try {
            UsuarioEntity usuarioEntity = new UsuarioEntity();
            usuarioEntity.setEmail(usuarioRequest.getEmail());
            usuarioEntity.setContrasenia(passwordEncoder.encode(usuarioRequest.getContrasenia()));
            List<String> roles = usuarioRequest.getRoles();
            usuarioService.guardarUsuario(usuarioEntity, roles);
            return ResponseEntity.ok("Usuario creado correctamente con los roles: " + roles);
        } catch (EmailExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo electrónico ya existe.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uno o más roles no existen.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el usuario.");
        }
    }

    @PreAuthorize("hasAnyRole('ROL_ADMIN', 'ROL_AUXILIAR')")
    @GetMapping("/prueba")
    public String prueba(){
        return "Estas autorizado";
    }
}

