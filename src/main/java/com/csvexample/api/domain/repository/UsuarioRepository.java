package com.csvexample.api.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.csvexample.api.domain.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
}
