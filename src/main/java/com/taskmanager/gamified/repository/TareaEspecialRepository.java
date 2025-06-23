package com.taskmanager.gamified.repository;

import com.taskmanager.gamified.entity.TareaEspecial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TareaEspecialRepository extends JpaRepository<TareaEspecial, Long> {
    
    @Query("SELECT te FROM TareaEspecial te WHERE te.activa = true AND " +
           "(te.fechaLimite IS NULL OR te.fechaLimite > :fecha)")
    List<TareaEspecial> findTareasEspecialesVigentes(@Param("fecha") LocalDateTime fecha);
    
    @Query("SELECT te FROM TareaEspecial te WHERE te.activa = true AND " +
           "te.fechaLimite IS NOT NULL AND te.fechaLimite <= :fecha")
    List<TareaEspecial> findTareasEspecialesVencidas(@Param("fecha") LocalDateTime fecha);
    
    @Query("SELECT CASE WHEN COUNT(ht) > 0 THEN true ELSE false END " +
           "FROM HistorialTarea ht WHERE ht.tarea.id = :tareaId AND ht.usuario.id = :usuarioId")
    boolean existeCompletacionPorUsuario(@Param("tareaId") Long tareaId, @Param("usuarioId") Long usuarioId);
}