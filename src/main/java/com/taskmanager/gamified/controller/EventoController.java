package com.taskmanager.gamified.controller;

import com.taskmanager.gamified.dto.EventoDTO;
import com.taskmanager.gamified.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*") // Para desarrollo - configurar apropiadamente en producción
public class EventoController {

    @Autowired
    private EventoService eventoService;

    /**
     * Obtiene todos los eventos vigentes actualmente
     */
    @GetMapping("/vigentes")
    public ResponseEntity<List<EventoDTO>> obtenerEventosVigentes() {
        List<EventoDTO> eventos = eventoService.obtenerEventosVigentes();
        return ResponseEntity.ok(eventos);
    }

    /**
     * Obtiene todos los eventos (activos e inactivos)
     */
    @GetMapping
    public ResponseEntity<List<EventoDTO>> obtenerTodosLosEventos() {
        List<EventoDTO> eventos = eventoService.obtenerTodosLosEventos();
        return ResponseEntity.ok(eventos);
    }

    /**
     * Obtiene un evento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventoDTO> obtenerEventoPorId(@PathVariable Long id) {
        return eventoService.obtenerEventoPorId(id)
                .map(evento -> ResponseEntity.ok(evento))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo evento
     */
    @PostMapping
    public ResponseEntity<?> crearEvento(@RequestBody Map<String, Object> request) {
        try {
            String nombre = (String) request.get("nombre");
            String descripcion = (String) request.get("descripcion");
            Double multiplicador = ((Number) request.get("multiplicador")).doubleValue();
            String fechaInicioStr = (String) request.get("fechaInicio");
            String fechaFinStr = (String) request.get("fechaFin");

            if (nombre == null || descripcion == null || multiplicador == null 
                || fechaInicioStr == null || fechaFinStr == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Todos los campos son requeridos"));
            }

            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime fechaInicio = LocalDateTime.parse(fechaInicioStr, formatter);
            LocalDateTime fechaFin = LocalDateTime.parse(fechaFinStr, formatter);

            EventoDTO evento = eventoService.crearEvento(nombre, descripcion, multiplicador, 
                                                        fechaInicio, fechaFin);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(evento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Error en el formato de datos: " + e.getMessage()));
        }
    }

    /**
     * Activa o desactiva un evento
     */
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstadoEvento(
            @PathVariable Long id, 
            @RequestBody Map<String, Boolean> request) {
        try {
            Boolean activo = request.get("activo");
            if (activo == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El campo 'activo' es requerido"));
            }

            EventoDTO evento = eventoService.actualizarEstadoEvento(id, activo);
            return ResponseEntity.ok(evento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtiene el multiplicador efectivo actual
     */
    @GetMapping("/multiplicador-actual")
    public ResponseEntity<Map<String, Object>> obtenerMultiplicadorActual() {
        double multiplicador = eventoService.obtenerMultiplicadorEfectivoActual();
        boolean hayEventos = eventoService.hayEventosVigentes();
        
        return ResponseEntity.ok(Map.of(
                "multiplicador", multiplicador,
                "hayEventosVigentes", hayEventos,
                "timestamp", LocalDateTime.now()
        ));
    }

    /**
     * Verifica si hay eventos vigentes
     */
    @GetMapping("/hay-vigentes")
    public ResponseEntity<Map<String, Boolean>> hayEventosVigentes() {
        boolean hayEventos = eventoService.hayEventosVigentes();
        return ResponseEntity.ok(Map.of("hayEventosVigentes", hayEventos));
    }

    /**
     * Obtiene eventos vigentes en una fecha específica
     */
    @GetMapping("/en-fecha")
    public ResponseEntity<List<EventoDTO>> obtenerEventosEnFecha(
            @RequestParam String fecha) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime fechaConsulta = LocalDateTime.parse(fecha, formatter);
            
            List<EventoDTO> eventos = eventoService.obtenerEventosEnFecha(fechaConsulta);
            return ResponseEntity.ok(eventos);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Extiende la duración de un evento
     */
    @PutMapping("/{id}/extender")
    public ResponseEntity<?> extenderEvento(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String nuevaFechaFinStr = request.get("nuevaFechaFin");
            if (nuevaFechaFinStr == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El campo 'nuevaFechaFin' es requerido"));
            }

            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime nuevaFechaFin = LocalDateTime.parse(nuevaFechaFinStr, formatter);

            EventoDTO evento = eventoService.extenderEvento(id, nuevaFechaFin);
            return ResponseEntity.ok(evento);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Obtiene estadísticas de eventos
     */
    @GetMapping("/estadisticas")
    public ResponseEntity<EventoService.EstadisticasEventos> obtenerEstadisticasEventos() {
        EventoService.EstadisticasEventos estadisticas = eventoService.obtenerEstadisticasEventos();
        return ResponseEntity.ok(estadisticas);
    }

    /**
     * Health check específico para eventos
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        EventoService.EstadisticasEventos stats = eventoService.obtenerEstadisticasEventos();
        
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "service", "EventoController",
                "timestamp", LocalDateTime.now(),
                "totalEventos", stats.getTotalEventos(),
                "eventosVigentes", stats.getEventosVigentes(),
                "multiplicadorActual", eventoService.obtenerMultiplicadorEfectivoActual()
        ));
    }
}