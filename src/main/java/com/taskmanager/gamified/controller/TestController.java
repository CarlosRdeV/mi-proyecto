package com.taskmanager.gamified.controller;

import com.taskmanager.gamified.dto.*;
import com.taskmanager.gamified.entity.Usuario;
import com.taskmanager.gamified.mapper.UsuarioMapper;
import com.taskmanager.gamified.mapper.TareaMapper;
import com.taskmanager.gamified.mapper.EventoMapper;
import com.taskmanager.gamified.repository.UsuarioRepository;
import com.taskmanager.gamified.repository.TareaRepository;
import com.taskmanager.gamified.repository.EventoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private TareaRepository tareaRepository;
    
    @Autowired
    private EventoRepository eventoRepository;
    
    @Autowired
    private UsuarioMapper usuarioMapper;
    
    @Autowired
    private TareaMapper tareaMapper;
    
    @Autowired
    private EventoMapper eventoMapper;
    
    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of(
            "status", "OK",
            "timestamp", LocalDateTime.now(),
            "message", "Task Manager Gamificado funcionando correctamente"
        );
    }
    
    @GetMapping("/usuarios")
    public List<UsuarioDTO> obtenerUsuarios() {
        return usuarioMapper.toDTO(usuarioRepository.findAll());
    }
    
    @GetMapping("/tareas")
    public List<TareaDTO> obtenerTareas() {
        return tareaMapper.toDTO(tareaRepository.findByActivaTrueOrderByDificultadAscNombreAsc());
    }
    
    @GetMapping("/eventos")
    public List<EventoDTO> obtenerEventos() {
        return eventoMapper.toDTO(eventoRepository.findEventosVigentes(LocalDateTime.now()));
    }
    
    @GetMapping("/stats")
    public EstadisticasDTO obtenerEstadisticas() {
        long totalUsuarios = usuarioRepository.count();
        long totalTareas = tareaRepository.count();
        long totalEventos = eventoRepository.count();
        long tareasActivas = tareaRepository.findByActivaTrueOrderByDificultadAscNombreAsc().size();
        long eventosVigentes = eventoRepository.findEventosVigentes(LocalDateTime.now()).size();
        
        return new EstadisticasDTO(totalUsuarios, totalTareas, totalEventos, tareasActivas, eventosVigentes);
    }
    
    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        try {
            String nombre = request.getNombre().trim();
            
            if (usuarioRepository.existsByNombre(nombre)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Ya existe un usuario con ese nombre"));
            }
            
            Usuario usuario = new Usuario(nombre);
            Usuario usuarioGuardado = usuarioRepository.save(usuario);
            
            return ResponseEntity.ok(usuarioMapper.toDTO(usuarioGuardado));
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor: " + e.getMessage()));
        }
    }
}