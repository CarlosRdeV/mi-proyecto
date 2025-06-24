package com.taskmanager.gamified.controller;

import com.taskmanager.gamified.dto.UsuarioDTO;
import com.taskmanager.gamified.dto.CrearUsuarioRequest;
import com.taskmanager.gamified.service.UsuarioService;
import com.taskmanager.gamified.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*") // Para desarrollo - configurar apropiadamente en producción
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private TareaService tareaService;

    /**
     * Obtiene todos los usuarios
     */
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtiene un usuario por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerUsuarioPorId(id)
                .map(usuario -> ResponseEntity.ok(usuario))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene un usuario por nombre
     */
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorNombre(@PathVariable String nombre) {
        return usuarioService.obtenerUsuarioPorNombre(nombre)
                .map(usuario -> ResponseEntity.ok(usuario))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo usuario
     */
    @PostMapping
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        try {
            UsuarioDTO usuario = usuarioService.crearUsuario(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Actualiza la fecha de último login del usuario
     */
    @PutMapping("/{id}/login")
    public ResponseEntity<?> actualizarUltimoLogin(@PathVariable Long id) {
        try {
            usuarioService.actualizarUltimoLogin(id);
            return ResponseEntity.ok(Map.of("message", "Login actualizado correctamente"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene el progreso hacia el siguiente nivel
     */
    @GetMapping("/{id}/progreso-nivel")
    public ResponseEntity<UsuarioService.ProgresoNivel> obtenerProgresoNivel(@PathVariable Long id) {
        try {
            UsuarioService.ProgresoNivel progreso = usuarioService.obtenerProgresoNivel(id);
            return ResponseEntity.ok(progreso);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Verifica si el usuario ha completado al menos una tarea hoy
     */
    @GetMapping("/{id}/tarea-hoy")
    public ResponseEntity<Map<String, Boolean>> haCompletadoTareaHoy(@PathVariable Long id) {
        try {
            boolean completado = usuarioService.haCompletadoTareaHoy(id);
            return ResponseEntity.ok(Map.of("completadoHoy", completado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene estadísticas de tareas para un usuario
     */
    @GetMapping("/{id}/estadisticas-tareas")
    public ResponseEntity<TareaService.EstadisticasTareas> obtenerEstadisticasTareas(@PathVariable Long id) {
        try {
            TareaService.EstadisticasTareas estadisticas = tareaService.obtenerEstadisticasTareas(id);
            return ResponseEntity.ok(estadisticas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Agrega experiencia manualmente a un usuario (para testing/admin)
     */
    @PutMapping("/{id}/experiencia")
    public ResponseEntity<?> agregarExperiencia(
            @PathVariable Long id, 
            @RequestBody Map<String, Integer> request) {
        try {
            Integer xp = request.get("xp");
            if (xp == null || xp <= 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "XP debe ser un número positivo"));
            }
            
            UsuarioDTO usuario = usuarioService.agregarExperiencia(id, xp);
            return ResponseEntity.ok(usuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Health check específico para usuarios
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "service", "UsuarioController",
                "timestamp", java.time.LocalDateTime.now(),
                "totalUsuarios", usuarioService.obtenerTodosLosUsuarios().size()
        ));
    }
}