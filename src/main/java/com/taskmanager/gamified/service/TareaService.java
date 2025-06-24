package com.taskmanager.gamified.service;

import com.taskmanager.gamified.dto.TareaDTO;
import com.taskmanager.gamified.dto.UsuarioDTO;
import com.taskmanager.gamified.entity.*;
import com.taskmanager.gamified.repository.*;
import com.taskmanager.gamified.mapper.TareaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private HistorialTareaRepository historialTareaRepository;
    
    @Autowired
    private EventoRepository eventoRepository;
    
    @Autowired
    private TareaMapper tareaMapper;
    
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Obtiene todas las tareas activas ordenadas por dificultad
     */
    @Transactional(readOnly = true)
    public List<TareaDTO> obtenerTareasActivas() {
        List<Tarea> tareas = tareaRepository.findByActivaTrueOrderByDificultad();
        return tareas.stream()
                .map(tareaMapper::toDTO)
                .toList();
    }

    /**
     * Obtiene una tarea por ID
     */
    @Transactional(readOnly = true)
    public Optional<TareaDTO> obtenerTareaPorId(Long id) {
        return tareaRepository.findById(id)
                .map(tareaMapper::toDTO);
    }

    /**
     * Obtiene tareas disponibles para un usuario
     * (excluye tareas especiales ya completadas y tareas repetibles completadas hoy)
     */
    @Transactional(readOnly = true)
    public List<TareaDTO> obtenerTareasDisponiblesPara(Long usuarioId) {
        List<Tarea> todasLasTareas = tareaRepository.findByActivaTrueOrderByDificultad();
        LocalDateTime inicioDelDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDelDia = inicioDelDia.plusDays(1);
        
        // Obtener tareas ya completadas hoy
        List<HistorialTarea> tareasCompletadasHoy = historialTareaRepository
                .findByUsuarioIdAndFechaCompletacionBetween(usuarioId, inicioDelDia, finDelDia);
        
        List<Long> idsCompletadasHoy = tareasCompletadasHoy.stream()
                .map(h -> h.getTarea().getId())
                .toList();
        
        // Obtener tareas especiales ya completadas por el usuario
        List<HistorialTarea> tareasEspecialesCompletadas = historialTareaRepository
                .findByUsuarioIdAndTareaTipo(usuarioId, "TareaEspecial");
        
        List<Long> idsEspecialesCompletadas = tareasEspecialesCompletadas.stream()
                .map(h -> h.getTarea().getId())
                .toList();

        return todasLasTareas.stream()
                .filter(tarea -> {
                    // Filtrar tareas especiales ya completadas
                    if (tarea instanceof TareaEspecial && idsEspecialesCompletadas.contains(tarea.getId())) {
                        return false;
                    }
                    
                    // Filtrar tareas repetibles completadas hoy
                    if (tarea.getRepetible() && idsCompletadasHoy.contains(tarea.getId())) {
                        return false;
                    }
                    
                    // Verificar fecha límite para tareas especiales
                    if (tarea instanceof TareaEspecial tareaEspecial) {
                        LocalDateTime fechaLimite = tareaEspecial.getFechaLimite();
                        if (fechaLimite != null && fechaLimite.isBefore(LocalDateTime.now())) {
                            return false; // Tarea especial expirada
                        }
                    }
                    
                    return true;
                })
                .map(tareaMapper::toDTO)
                .toList();
    }

    /**
     * Completa una tarea para un usuario específico
     */
    @Transactional
    public ResultadoCompletarTarea completarTarea(Long usuarioId, Long tareaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));
        
        Tarea tarea = tareaRepository.findById(tareaId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada: " + tareaId));

        if (!tarea.getActiva()) {
            throw new IllegalStateException("La tarea no está activa");
        }

        // Verificar si la tarea está disponible para este usuario
        List<TareaDTO> tareasDisponibles = obtenerTareasDisponiblesPara(usuarioId);
        boolean tareaDisponible = tareasDisponibles.stream()
                .anyMatch(t -> t.getId().equals(tareaId));
        
        if (!tareaDisponible) {
            throw new IllegalStateException("La tarea no está disponible para este usuario");
        }

        // Calcular XP base
        int xpBase = tarea.getDificultad() == DificultadTarea.ESPECIAL 
                ? ((TareaEspecial) tarea).getXpPersonalizado()
                : tarea.getDificultad().getXpBase();

        // Calcular multiplicadores
        double multiplicadorStreak = UsuarioService.calcularMultiplicadorStreak(usuario.getStreakActual());
        double multiplicadorEvento = obtenerMultiplicadorEventoActivo();
        
        // XP final
        int xpFinal = (int) Math.round(xpBase * multiplicadorStreak * multiplicadorEvento);

        // Crear entrada en historial
        HistorialTarea historial = new HistorialTarea();
        historial.setUsuario(usuario);
        historial.setTarea(tarea);
        historial.setFechaCompletacion(LocalDateTime.now());
        historial.setXpGanado(xpFinal);
        historial.setMultiplicadorStreak(multiplicadorStreak);
        historial.setMultiplicadorEvento(multiplicadorEvento);
        
        historialTareaRepository.save(historial);

        // Actualizar usuario con nueva experiencia
        UsuarioDTO usuarioActualizado = usuarioService.agregarExperiencia(usuarioId, xpFinal);
        
        // Actualizar último login si es necesario
        usuarioService.actualizarUltimoLogin(usuarioId);

        return new ResultadoCompletarTarea(
                usuarioActualizado,
                tareaMapper.toDTO(tarea),
                xpBase,
                xpFinal,
                multiplicadorStreak,
                multiplicadorEvento
        );
    }

    /**
     * Obtiene el multiplicador de evento activo actualmente
     */
    private double obtenerMultiplicadorEventoActivo() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Evento> eventosActivos = eventoRepository.findEventosVigentes(ahora);
        
        // Si hay múltiples eventos, aplicar el de mayor multiplicador
        return eventosActivos.stream()
                .mapToDouble(Evento::getMultiplicador)
                .max()
                .orElse(1.0);
    }

    /**
     * Obtiene el historial de tareas completadas por un usuario
     */
    @Transactional(readOnly = true)
    public List<HistorialTarea> obtenerHistorialUsuario(Long usuarioId, int limite) {
        return historialTareaRepository.findByUsuarioIdOrderByFechaCompletacionDesc(usuarioId)
                .stream()
                .limit(limite)
                .toList();
    }

    /**
     * Verifica si un usuario puede completar una tarea específica
     */
    @Transactional(readOnly = true)
    public boolean puedeCompletarTarea(Long usuarioId, Long tareaId) {
        try {
            List<TareaDTO> tareasDisponibles = obtenerTareasDisponiblesPara(usuarioId);
            return tareasDisponibles.stream()
                    .anyMatch(t -> t.getId().equals(tareaId));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Obtiene estadísticas de tareas para un usuario
     */
    @Transactional(readOnly = true)
    public EstadisticasTareas obtenerEstadisticasTareas(Long usuarioId) {
        List<HistorialTarea> historialCompleto = historialTareaRepository
                .findByUsuarioIdOrderByFechaCompletacionDesc(usuarioId);

        if (historialCompleto.isEmpty()) {
            return new EstadisticasTareas(0, 0, null, 0.0);
        }

        int totalCompletadas = historialCompleto.size();
        int xpTotalGanado = historialCompleto.stream()
                .mapToInt(HistorialTarea::getXpGanado)
                .sum();

        // Encontrar tarea más completada
        var tareasFrecuencia = historialCompleto.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        h -> h.getTarea().getNombre(),
                        java.util.stream.Collectors.counting()
                ));

        String tareaMasCompletada = tareasFrecuencia.entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(java.util.Map.Entry::getKey)
                .orElse(null);

        // Calcular promedio de XP por tarea
        double promedioXp = (double) xpTotalGanado / totalCompletadas;

        return new EstadisticasTareas(totalCompletadas, xpTotalGanado, tareaMasCompletada, promedioXp);
    }

    /**
     * DTO para el resultado de completar una tarea
     */
    public static class ResultadoCompletarTarea {
        private final UsuarioDTO usuario;
        private final TareaDTO tarea;
        private final int xpBase;
        private final int xpFinal;
        private final double multiplicadorStreak;
        private final double multiplicadorEvento;

        public ResultadoCompletarTarea(UsuarioDTO usuario, TareaDTO tarea, int xpBase, 
                                     int xpFinal, double multiplicadorStreak, double multiplicadorEvento) {
            this.usuario = usuario;
            this.tarea = tarea;
            this.xpBase = xpBase;
            this.xpFinal = xpFinal;
            this.multiplicadorStreak = multiplicadorStreak;
            this.multiplicadorEvento = multiplicadorEvento;
        }

        public UsuarioDTO getUsuario() { return usuario; }
        public TareaDTO getTarea() { return tarea; }
        public int getXpBase() { return xpBase; }
        public int getXpFinal() { return xpFinal; }
        public double getMultiplicadorStreak() { return multiplicadorStreak; }
        public double getMultiplicadorEvento() { return multiplicadorEvento; }
    }

    /**
     * DTO para estadísticas de tareas
     */
    public static class EstadisticasTareas {
        private final int totalCompletadas;
        private final int xpTotalGanado;
        private final String tareaMasCompletada;
        private final double promedioXpPorTarea;

        public EstadisticasTareas(int totalCompletadas, int xpTotalGanado, 
                                String tareaMasCompletada, double promedioXpPorTarea) {
            this.totalCompletadas = totalCompletadas;
            this.xpTotalGanado = xpTotalGanado;
            this.tareaMasCompletada = tareaMasCompletada;
            this.promedioXpPorTarea = promedioXpPorTarea;
        }

        public int getTotalCompletadas() { return totalCompletadas; }
        public int getXpTotalGanado() { return xpTotalGanado; }
        public String getTareaMasCompletada() { return tareaMasCompletada; }
        public double getPromedioXpPorTarea() { return promedioXpPorTarea; }
    }
}