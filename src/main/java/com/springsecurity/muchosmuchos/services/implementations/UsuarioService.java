package com.springsecurity.muchosmuchos.services.implementations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springsecurity.muchosmuchos.exception.EmailExistsException;
import com.springsecurity.muchosmuchos.model.entity.RolEntity;
import com.springsecurity.muchosmuchos.model.entity.UsuarioEntity;
import com.springsecurity.muchosmuchos.repository.RolRepository;
import com.springsecurity.muchosmuchos.repository.UsuarioRepository;
import com.springsecurity.muchosmuchos.services.interfaces.UsuarioInterface;

@Service
public class UsuarioService implements UsuarioInterface{

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioEntity userEntity = usuarioRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        Set<String> roles = userEntity.getRoles()
                .stream()
                .map(role -> role.getNombre()) 
                .collect(Collectors.toSet());

        Set<SimpleGrantedAuthority> authorities = roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        return new User(userEntity.getEmail(),
                userEntity.getContrasenia(),
                authorities);
    }

    @Override
    @Transactional
    public void guardarUsuario(UsuarioEntity usuarioEntity, List<String> rolesNombres) {
        if (usuarioRepository.findByEmail(usuarioEntity.getEmail()).isPresent()) {
            throw new EmailExistsException("El correo electrónico ya existe");
        }

        Set<RolEntity> roles = new HashSet<>();
        for (String rolNombre : rolesNombres) {
            RolEntity rolEntity = rolRepository.findByNombre(rolNombre).orElseThrow(
                () -> new RuntimeException("El rol '" + rolNombre + "' no existe.")
            );
            roles.add(rolEntity);
        }
        usuarioEntity.setRoles(roles);
        usuarioRepository.save(usuarioEntity);
    }
    
}
