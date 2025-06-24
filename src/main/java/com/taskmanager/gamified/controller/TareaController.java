package com.taskmanager.gamified.controller;

import com.taskmanager.gamified.dto.TareaDTO;
import com.taskmanager.gamified.entity.HistorialTarea;
import com.taskmanager.gamified.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*") // Para desarrollo - configurar apropiadamente en producción
public class TareaController {

    @Autowired
    private TareaService tareaService;

    /**
     * Obtiene todas las tareas activas
     */
    @GetMapping
    public ResponseEntity<List<TareaDTO>> obtenerTareasActivas() {
        List<TareaDTO> tareas = tareaService.obtenerTareasActivas();
        return ResponseEntity.ok(tareas);
    }

    /**
     * Obtiene una tarea por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TareaDTO> obtenerTareaPorId(@PathVariable Long id) {
        return tareaService.obtenerTareaPorId(id)
                .map(tarea -> ResponseEntity.ok(tarea))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene tareas disponibles para un usuario específico
     */
    @GetMapping("/disponibles/{usuarioId}")
    public ResponseEntity<List<TareaDTO>> obtenerTareasDisponibles(@PathVariable Long usuarioId) {
        try {
            List<TareaDTO> tareas = tareaService.obtenerTareasDisponiblesPara(usuarioId);
            return ResponseEntity.ok(tareas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Completa una tarea para un usuario
     */
    @PostMapping("/{id}/completar")
    public ResponseEntity<?> completarTarea(
            @PathVariable Long id, 
            @RequestBody Map<String, Long> request) {
        try {
            Long usuarioId = request.get("usuarioId");
            if (usuarioId == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "usuarioId es requerido"));
            }

            TareaService.ResultadoCompletarTarea resultado = 
                    tareaService.completarTarea(usuarioId, id);
            
            return ResponseEntity.ok(Map.of(
                    "usuario", resultado.getUsuario(),
                    "tarea", resultado.getTarea(),
                    "xpBase", resultado.getXpBase(),
                    "xpFinal", resultado.getXpFinal(),
                    "multiplicadorStreak", resultado.getMultiplicadorStreak(),
                    "multiplicadorEvento", resultado.getMultiplicadorEvento(),
                    "mensaje", "¡Tarea completada exitosamente!"
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Verifica si un usuario puede completar una tarea específica
     */
    @GetMapping("/{id}/puede-completar/{usuarioId}")
    public ResponseEntity<Map<String, Boolean>> puedeCompletarTarea(
            @PathVariable Long id, 
            @PathVariable Long usuarioId) {
        boolean puede = tareaService.puedeCompletarTarea(usuarioId, id);
        return ResponseEntity.ok(Map.of("puedeCompletar", puede));
    }

    /**
     * Obtiene el historial de tareas completadas por un usuario
     */
    @GetMapping("/historial/{usuarioId}")
    public ResponseEntity<List<HistorialTarea>> obtenerHistorialUsuario(
            @PathVariable Long usuarioId,
            @RequestParam(defaultValue = "10") int limite) {
        try {
            List<HistorialTarea> historial = tareaService.obtenerHistorialUsuario(usuarioId, limite);
            return ResponseEntity.ok(historial);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene estadísticas de tareas para un usuario
     */
    @GetMapping("/estadisticas/{usuarioId}")
    public ResponseEntity<TareaService.EstadisticasTareas> obtenerEstadisticasTareas(
            @PathVariable Long usuarioId) {
        try {
            TareaService.EstadisticasTareas estadisticas = 
                    tareaService.obtenerEstadisticasTareas(usuarioId);
            return ResponseEntity.ok(estadisticas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Health check específico para tareas
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        List<TareaDTO> tareas = tareaService.obtenerTareasActivas();
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "service", "TareaController",
                "timestamp", java.time.LocalDateTime.now(),
                "totalTareasActivas", tareas.size()
        ));
    }
}