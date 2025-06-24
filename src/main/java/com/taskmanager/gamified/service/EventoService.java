package com.taskmanager.gamified.service;

import com.taskmanager.gamified.dto.EventoDTO;
import com.taskmanager.gamified.entity.Evento;
import com.taskmanager.gamified.repository.EventoRepository;
import com.taskmanager.gamified.mapper.EventoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;
    
    @Autowired
    private EventoMapper eventoMapper;

    /**
     * Obtiene todos los eventos vigentes (activos y dentro del rango de fechas)
     */
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosVigentes() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Evento> eventos = eventoRepository.findEventosVigentes(ahora);
        return eventos.stream()
                .map(eventoMapper::toDTO)
                .toList();
    }

    /**
     * Obtiene todos los eventos (activos e inactivos)
     */
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerTodosLosEventos() {
        List<Evento> eventos = eventoRepository.findAll();
        return eventos.stream()
                .map(eventoMapper::toDTO)
                .toList();
    }

    /**
     * Obtiene un evento por ID
     */
    @Transactional(readOnly = true)
    public Optional<EventoDTO> obtenerEventoPorId(Long id) {
        return eventoRepository.findById(id)
                .map(eventoMapper::toDTO);
    }

    /**
     * Crea un nuevo evento
     */
    @Transactional
    public EventoDTO crearEvento(String nombre, String descripcion, double multiplicador, 
                                LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
        
        if (multiplicador <= 0) {
            throw new IllegalArgumentException("El multiplicador debe ser mayor que 0");
        }

        Evento evento = new Evento();
        evento.setNombre(nombre);
        evento.setDescripcion(descripcion);
        evento.setMultiplicador(multiplicador);
        evento.setFechaInicio(fechaInicio);
        evento.setFechaFin(fechaFin);
        evento.setActivo(true);

        evento = eventoRepository.save(evento);
        return eventoMapper.toDTO(evento);
    }

    /**
     * Activa o desactiva un evento
     */
    @Transactional
    public EventoDTO actualizarEstadoEvento(Long id, boolean activo) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado: " + id));

        evento.setActivo(activo);
        evento = eventoRepository.save(evento);
        
        return eventoMapper.toDTO(evento);
    }

    /**
     * Obtiene el multiplicador efectivo actual (mayor multiplicador de eventos vigentes)
     */
    @Transactional(readOnly = true)
    public double obtenerMultiplicadorEfectivoActual() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Evento> eventosVigentes = eventoRepository.findEventosVigentes(ahora);
        
        return eventosVigentes.stream()
                .mapToDouble(Evento::getMultiplicador)
                .max()
                .orElse(1.0);
    }

    /**
     * Verifica si hay eventos vigentes en este momento
     */
    @Transactional(readOnly = true)
    public boolean hayEventosVigentes() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Evento> eventosVigentes = eventoRepository.findEventosVigentes(ahora);
        return !eventosVigentes.isEmpty();
    }

    /**
     * Obtiene eventos que estarán activos en una fecha específica
     */
    @Transactional(readOnly = true)
    public List<EventoDTO> obtenerEventosEnFecha(LocalDateTime fecha) {
        List<Evento> eventos = eventoRepository.findEventosVigentes(fecha);
        return eventos.stream()
                .map(eventoMapper::toDTO)
                .toList();
    }

    /**
     * Extiende la duración de un evento
     */
    @Transactional
    public EventoDTO extenderEvento(Long id, LocalDateTime nuevaFechaFin) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado: " + id));

        if (nuevaFechaFin.isBefore(evento.getFechaInicio())) {
            throw new IllegalArgumentException("La nueva fecha de fin no puede ser anterior a la fecha de inicio");
        }

        evento.setFechaFin(nuevaFechaFin);
        evento = eventoRepository.save(evento);
        
        return eventoMapper.toDTO(evento);
    }

    /**
     * Obtiene estadísticas de eventos
     */
    @Transactional(readOnly = true)
    public EstadisticasEventos obtenerEstadisticasEventos() {
        List<Evento> todosLosEventos = eventoRepository.findAll();
        LocalDateTime ahora = LocalDateTime.now();
        
        long eventosActivos = todosLosEventos.stream()
                .filter(Evento::getActivo)
                .count();
        
        long eventosVigentes = eventoRepository.findEventosVigentes(ahora).size();
        
        double multiplicadorPromedio = todosLosEventos.stream()
                .filter(Evento::getActivo)
                .mapToDouble(Evento::getMultiplicador)
                .average()
                .orElse(1.0);

        return new EstadisticasEventos(
                todosLosEventos.size(),
                (int) eventosActivos,
                (int) eventosVigentes,
                multiplicadorPromedio
        );
    }

    /**
     * DTO para estadísticas de eventos
     */
    public static class EstadisticasEventos {
        private final int totalEventos;
        private final int eventosActivos;
        private final int eventosVigentes;
        private final double multiplicadorPromedio;

        public EstadisticasEventos(int totalEventos, int eventosActivos, 
                                 int eventosVigentes, double multiplicadorPromedio) {
            this.totalEventos = totalEventos;
            this.eventosActivos = eventosActivos;
            this.eventosVigentes = eventosVigentes;
            this.multiplicadorPromedio = multiplicadorPromedio;
        }

        public int getTotalEventos() { return totalEventos; }
        public int getEventosActivos() { return eventosActivos; }
        public int getEventosVigentes() { return eventosVigentes; }
        public double getMultiplicadorPromedio() { return multiplicadorPromedio; }
    }
}