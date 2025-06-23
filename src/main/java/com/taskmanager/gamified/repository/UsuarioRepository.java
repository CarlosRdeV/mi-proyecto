package com.taskmanager.gamified.repository;

import com.taskmanager.gamified.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    @Query("SELECT u FROM Usuario u WHERE u.nivel = :nivel ORDER BY u.experiencia DESC")
    java.util.List<Usuario> findByNivelOrderByExperienciaDesc(@Param("nivel") Integer nivel);
    
    @Query("SELECT u FROM Usuario u ORDER BY u.nivel DESC, u.experiencia DESC")
    java.util.List<Usuario> findAllOrderByNivelAndExperiencia();
}