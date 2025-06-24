package com.taskmanager.gamified.controller;

import com.taskmanager.gamified.dto.EstadisticasDTO;
import com.taskmanager.gamified.service.EstadisticasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats")
@CrossOrigin(origins = "*") // Para desarrollo - configurar apropiadamente en producción
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    /**
     * Obtiene estadísticas generales del sistema
     */
    @GetMapping
    public ResponseEntity<EstadisticasDTO> obtenerEstadisticasGenerales() {
        EstadisticasDTO estadisticas = estadisticasService.obtenerEstadisticasGenerales();
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Obtiene estadísticas avanzadas del sistema
     */
    @GetMapping("/avanzadas")
    public ResponseEntity<EstadisticasService.EstadisticasAvanzadas> obtenerEstadisticasAvanzadas() {
        EstadisticasService.EstadisticasAvanzadas estadisticas = 
                estadisticasService.obtenerEstadisticasAvanzadas();
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Obtiene estadísticas de niveles
     */
    @GetMapping("/niveles")
    public ResponseEntity<EstadisticasService.EstadisticasNiveles> obtenerEstadisticasNiveles() {
        EstadisticasService.EstadisticasNiveles estadisticas = 
                estadisticasService.calcularEstadisticasNiveles();
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Obtiene estadísticas de streaks
     */
    @GetMapping("/streaks")
    public ResponseEntity<EstadisticasService.EstadisticasStreaks> obtenerEstadisticasStreaks() {
        EstadisticasService.EstadisticasStreaks estadisticas = 
                estadisticasService.calcularEstadisticasStreaks();
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Obtiene actividad por día de la semana
     */
    @GetMapping("/actividad-semanal")
    public ResponseEntity<Map<String, Integer>> obtenerActividadPorDiaSemana() {
        Map<String, Integer> actividad = estadisticasService.obtenerActividadPorDiaSemana();
        return ResponseEntity.ok(actividad);
    }

    /**
     * Obtiene top de usuarios por XP
     */
    @GetMapping("/top-usuarios")
    public ResponseEntity<List<EstadisticasService.TopUsuario>> obtenerTopUsuarios(
            @RequestParam(defaultValue = "10") int limite) {
        List<EstadisticasService.TopUsuario> topUsuarios = 
                estadisticasService.obtenerTopUsuariosPorXP(limite);
        return ResponseEntity.ok(topUsuarios);
    }

    /**
     * Obtiene distribución de dificultades
     */
    @GetMapping("/distribucion-dificultades")
    public ResponseEntity<Map<String, Integer>> obtenerDistribucionDificultades() {
        Map<String, Integer> distribucion = estadisticasService.obtenerDistribucionDificultades();
        return ResponseEntity.ok(distribucion);
    }

    /**
     * Dashboard completo de estadísticas
     */
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> obtenerDashboardCompleto() {
        EstadisticasDTO estadisticasGenerales = estadisticasService.obtenerEstadisticasGenerales();
        EstadisticasService.EstadisticasAvanzadas estadisticasAvanzadas = 
                estadisticasService.obtenerEstadisticasAvanzadas();
        List<EstadisticasService.TopUsuario> topUsuarios = 
                estadisticasService.obtenerTopUsuariosPorXP(5);
        Map<String, Integer> actividadSemanal = estadisticasService.obtenerActividadPorDiaSemana();
        Map<String, Integer> distribucionDificultades = 
                estadisticasService.obtenerDistribucionDificultades();

        return ResponseEntity.ok(Map.of(
                "estadisticasGenerales", estadisticasGenerales,
                "estadisticasAvanzadas", estadisticasAvanzadas,
                "topUsuarios", topUsuarios,
                "actividadSemanal", actividadSemanal,
                "distribucionDificultades", distribucionDificultades,
                "timestamp", LocalDateTime.now()
        ));
    }

    /**
     * Health check específico para estadísticas
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        EstadisticasDTO stats = estadisticasService.obtenerEstadisticasGenerales();
        
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "service", "EstadisticasController",
                "timestamp", LocalDateTime.now(),
                "totalRegistros", stats.getTotalUsuarios() + stats.getTotalTareas() + 
                                 stats.getTotalEventos() + stats.getTotalCompletaciones()
        ));
    }
}