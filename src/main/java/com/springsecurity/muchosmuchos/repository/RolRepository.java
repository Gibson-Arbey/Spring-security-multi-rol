package com.springsecurity.muchosmuchos.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.springsecurity.muchosmuchos.model.entity.RolEntity;

@Repository
public interface RolRepository extends JpaRepository<RolEntity, Long>{
    
    Optional<RolEntity> findByNombre(String nombre);
}
