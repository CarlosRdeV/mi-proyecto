package com.taskmanager.gamified.repository;

import com.taskmanager.gamified.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {
    
    @Query("SELECT e FROM Evento e WHERE e.activo = true AND " +
           "e.fechaInicio <= :fecha AND e.fechaFin > :fecha")
    List<Evento> findEventosVigentes(@Param("fecha") LocalDateTime fecha);
    
    @Query("SELECT e FROM Evento e WHERE e.activo = true AND e.fechaInicio > :fecha")
    List<Evento> findEventosFuturos(@Param("fecha") LocalDateTime fecha);
    
    @Query("SELECT e FROM Evento e WHERE e.fechaFin <= :fecha")
    List<Evento> findEventosTerminados(@Param("fecha") LocalDateTime fecha);
}