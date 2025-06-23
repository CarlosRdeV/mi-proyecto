package com.taskmanager.gamified.repository;

import com.taskmanager.gamified.entity.HistorialTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistorialTareaRepository extends JpaRepository<HistorialTarea, Long> {
    
    @Query("SELECT ht FROM HistorialTarea ht WHERE ht.usuario.id = :usuarioId " +
           "ORDER BY ht.fechaCompletacion DESC")
    List<HistorialTarea> findByUsuarioIdOrderByFechaDesc(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT ht FROM HistorialTarea ht WHERE ht.usuario.id = :usuarioId " +
           "AND ht.fechaCompletacion >= :fechaDesde ORDER BY ht.fechaCompletacion DESC")
    List<HistorialTarea> findByUsuarioIdAndFechaDesde(@Param("usuarioId") Long usuarioId, 
                                                       @Param("fechaDesde") LocalDateTime fechaDesde);
    
    @Query("SELECT ht FROM HistorialTarea ht WHERE ht.usuario.id = :usuarioId " +
           "AND DATE(ht.fechaCompletacion) = DATE(:fecha)")
    List<HistorialTarea> findByUsuarioIdAndFecha(@Param("usuarioId") Long usuarioId, 
                                                  @Param("fecha") LocalDateTime fecha);
    
    @Query("SELECT ht FROM HistorialTarea ht WHERE ht.usuario.id = :usuarioId " +
           "AND ht.tarea.id = :tareaId AND DATE(ht.fechaCompletacion) = DATE(:fecha)")
    List<HistorialTarea> findByUsuarioIdAndTareaIdAndFecha(@Param("usuarioId") Long usuarioId,
                                                            @Param("tareaId") Long tareaId,
                                                            @Param("fecha") LocalDateTime fecha);
    
    @Query("SELECT t.nombre, COUNT(ht) as cantidad FROM HistorialTarea ht " +
           "JOIN ht.tarea t WHERE ht.usuario.id = :usuarioId " +
           "GROUP BY t.id, t.nombre ORDER BY cantidad DESC")
    List<Object[]> findTareaMasCompletadaPorUsuario(@Param("usuarioId") Long usuarioId);
    
    @Query("DELETE FROM HistorialTarea ht WHERE ht.fechaCompletacion < :fecha")
    void deleteHistorialAnteriorA(@Param("fecha") LocalDateTime fecha);
}