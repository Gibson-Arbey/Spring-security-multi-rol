package com.springsecurity.muchosmuchos.services.implementations;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springsecurity.muchosmuchos.entity.UsuarioEntity;
import com.springsecurity.muchosmuchos.repository.UsuarioRepository;
import com.springsecurity.muchosmuchos.services.interfaces.UsuarioInterface;

@Service
public class UsuarioService implements UsuarioInterface{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity userEntity = usuarioRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        Set<String> roles = userEntity.getRoles()
                .stream()
                .map(role -> "ROLE_" + role.getName()) // Asegúrate de que role.getName() devuelva el nombre del rol como texto
                .collect(Collectors.toSet());

        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new User(userEntity.getEmail(),
                userEntity.getContrasenia(),
                authorities);
    }
    
}
