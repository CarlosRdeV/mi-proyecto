package com.taskmanager.gamified.repository;

import com.taskmanager.gamified.entity.Tarea;
import com.taskmanager.gamified.entity.DificultadTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long> {
    
    @Query("SELECT t FROM Tarea t WHERE t.activa = true ORDER BY " +
           "CASE t.dificultad " +
           "WHEN 'FACIL' THEN 1 " +
           "WHEN 'MEDIA' THEN 2 " +
           "WHEN 'DIFICIL' THEN 3 " +
           "WHEN 'ESPECIAL' THEN 4 " +
           "END, t.nombre ASC")
    List<Tarea> findByActivaTrueOrderByDificultadAscNombreAsc();
    
    // Alias para compatibilidad con servicios
    default List<Tarea> findByActivaTrueOrderByDificultad() {
        return findByActivaTrueOrderByDificultadAscNombreAsc();
    }
    
    List<Tarea> findByDificultadAndActivaTrue(DificultadTarea dificultad);
    
    @Query("SELECT t FROM Tarea t WHERE t.activa = true AND TYPE(t) = Tarea")
    List<Tarea> findTareasNormalesActivas();
    
    @Query("SELECT COUNT(t) FROM Tarea t WHERE t.activa = true AND t.dificultad = :dificultad")
    Long countByDificultadAndActivaTrue(@Param("dificultad") DificultadTarea dificultad);
}