package com.taskmanager.gamified.service;

import com.taskmanager.gamified.dto.EstadisticasDTO;
import com.taskmanager.gamified.entity.HistorialTarea;
import com.taskmanager.gamified.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private TareaRepository tareaRepository;
    
    @Autowired
    private EventoRepository eventoRepository;
    
    @Autowired
    private HistorialTareaRepository historialTareaRepository;

    /**
     * Obtiene estadísticas generales del sistema
     */
    @Transactional(readOnly = true)
    public EstadisticasDTO obtenerEstadisticasGenerales() {
        LocalDateTime timestamp = LocalDateTime.now();
        
        long totalUsuarios = usuarioRepository.count();
        long totalTareas = tareaRepository.count();
        long totalEventos = eventoRepository.count();
        long totalCompletaciones = historialTareaRepository.count();

        return new EstadisticasDTO(
                totalUsuarios,
                totalTareas,
                totalEventos,
                totalCompletaciones,
                timestamp
        );
    }

    /**
     * Obtiene estadísticas avanzadas del sistema
     */
    @Transactional(readOnly = true)
    public EstadisticasAvanzadas obtenerEstadisticasAvanzadas() {
        List<HistorialTarea> historialCompleto = historialTareaRepository.findAll();
        
        // Estadísticas básicas
        int totalCompletaciones = historialCompleto.size();
        int xpTotalSistema = historialCompleto.stream()
                .mapToInt(HistorialTarea::getXpGanado)
                .sum();
        
        // Usuario más activo
        Map<Long, Long> completacionesPorUsuario = historialCompleto.stream()
                .collect(Collectors.groupingBy(
                        h -> h.getUsuario().getId(),
                        Collectors.counting()
                ));
        
        String usuarioMasActivo = completacionesPorUsuario.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    Long usuarioId = entry.getKey();
                    return usuarioRepository.findById(usuarioId)
                            .map(u -> u.getNombre() + " (" + entry.getValue() + " tareas)")
                            .orElse("Desconocido");
                })
                .orElse("Ninguno");

        // Tarea más popular
        Map<String, Long> completacionesPorTarea = historialCompleto.stream()
                .collect(Collectors.groupingBy(
                        h -> h.getTarea().getNombre(),
                        Collectors.counting()
                ));
        
        String tareaMasPopular = completacionesPorTarea.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " (" + entry.getValue() + " veces)")
                .orElse("Ninguna");

        // Estadísticas de actividad reciente (últimos 7 días)
        LocalDateTime hace7Dias = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        List<HistorialTarea> actividadReciente = historialCompleto.stream()
                .filter(h -> h.getFechaCompletacion().isAfter(hace7Dias))
                .toList();
        
        int completacionesUltimos7Dias = actividadReciente.size();
        int xpGanadoUltimos7Dias = actividadReciente.stream()
                .mapToInt(HistorialTarea::getXpGanado)
                .sum();

        // Estadísticas de niveles
        EstadisticasNiveles estadisticasNiveles = calcularEstadisticasNiveles();
        
        // Estadísticas de streaks
        EstadisticasStreaks estadisticasStreaks = calcularEstadisticasStreaks();

        return new EstadisticasAvanzadas(
                totalCompletaciones,
                xpTotalSistema,
                usuarioMasActivo,
                tareaMasPopular,
                completacionesUltimos7Dias,
                xpGanadoUltimos7Dias,
                estadisticasNiveles,
                estadisticasStreaks
        );
    }

    /**
     * Calcula estadísticas de niveles de usuarios
     */
    @Transactional(readOnly = true)
    public EstadisticasNiveles calcularEstadisticasNiveles() {
        var usuarios = usuarioRepository.findAll();
        
        if (usuarios.isEmpty()) {
            return new EstadisticasNiveles(0, 0, 0.0, 0);
        }

        int nivelMinimo = usuarios.stream()
                .mapToInt(u -> u.getNivel())
                .min()
                .orElse(1);
        
        int nivelMaximo = usuarios.stream()
                .mapToInt(u -> u.getNivel())
                .max()
                .orElse(1);
        
        double nivelPromedio = usuarios.stream()
                .mapToInt(u -> u.getNivel())
                .average()
                .orElse(1.0);

        long usuariosNivelMaximo = usuarios.stream()
                .filter(u -> u.getNivel() == nivelMaximo)
                .count();

        return new EstadisticasNiveles(nivelMinimo, nivelMaximo, nivelPromedio, (int) usuariosNivelMaximo);
    }

    /**
     * Calcula estadísticas de streaks de usuarios
     */
    @Transactional(readOnly = true)
    public EstadisticasStreaks calcularEstadisticasStreaks() {
        var usuarios = usuarioRepository.findAll();
        
        if (usuarios.isEmpty()) {
            return new EstadisticasStreaks(0, 0, 0.0, 0);
        }

        int streakMinimo = usuarios.stream()
                .mapToInt(u -> u.getStreakActual())
                .min()
                .orElse(0);
        
        int streakMaximo = usuarios.stream()
                .mapToInt(u -> u.getStreakActual())
                .max()
                .orElse(0);
        
        double streakPromedio = usuarios.stream()
                .mapToInt(u -> u.getStreakActual())
                .average()
                .orElse(0.0);

        long usuariosConStreak = usuarios.stream()
                .filter(u -> u.getStreakActual() > 0)
                .count();

        return new EstadisticasStreaks(streakMinimo, streakMaximo, streakPromedio, (int) usuariosConStreak);
    }

    /**
     * Obtiene estadísticas de actividad por día de la semana
     */
    @Transactional(readOnly = true)
    public Map<String, Integer> obtenerActividadPorDiaSemana() {
        List<HistorialTarea> historial = historialTareaRepository.findAll();
        
        return historial.stream()
                .collect(Collectors.groupingBy(
                        h -> h.getFechaCompletacion().getDayOfWeek().toString(),
                        Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));
    }

    /**
     * Obtiene top de usuarios por XP ganado
     */
    @Transactional(readOnly = true)
    public List<TopUsuario> obtenerTopUsuariosPorXP(int limite) {
        return usuarioRepository.findAll().stream()
                .map(usuario -> new TopUsuario(
                        usuario.getNombre(),
                        usuario.getNivel(),
                        usuario.getExperiencia(),
                        usuario.getStreakActual()
                ))
                .sorted((a, b) -> Integer.compare(b.experiencia, a.experiencia))
                .limit(limite)
                .toList();
    }

    /**
     * Obtiene distribución de dificultades de tareas completadas
     */
    @Transactional(readOnly = true)
    public Map<String, Integer> obtenerDistribucionDificultades() {
        List<HistorialTarea> historial = historialTareaRepository.findAll();
        
        return historial.stream()
                .collect(Collectors.groupingBy(
                        h -> h.getTarea().getDificultad().toString(),
                        Collectors.collectingAndThen(Collectors.counting(), Math::toIntExact)
                ));
    }

    // DTOs para estadísticas

    public static class EstadisticasAvanzadas {
        private final int totalCompletaciones;
        private final int xpTotalSistema;
        private final String usuarioMasActivo;
        private final String tareaMasPopular;
        private final int completacionesUltimos7Dias;
        private final int xpGanadoUltimos7Dias;
        private final EstadisticasNiveles niveles;
        private final EstadisticasStreaks streaks;

        public EstadisticasAvanzadas(int totalCompletaciones, int xpTotalSistema, 
                                   String usuarioMasActivo, String tareaMasPopular,
                                   int completacionesUltimos7Dias, int xpGanadoUltimos7Dias,
                                   EstadisticasNiveles niveles, EstadisticasStreaks streaks) {
            this.totalCompletaciones = totalCompletaciones;
            this.xpTotalSistema = xpTotalSistema;
            this.usuarioMasActivo = usuarioMasActivo;
            this.tareaMasPopular = tareaMasPopular;
            this.completacionesUltimos7Dias = completacionesUltimos7Dias;
            this.xpGanadoUltimos7Dias = xpGanadoUltimos7Dias;
            this.niveles = niveles;
            this.streaks = streaks;
        }

        // Getters
        public int getTotalCompletaciones() { return totalCompletaciones; }
        public int getXpTotalSistema() { return xpTotalSistema; }
        public String getUsuarioMasActivo() { return usuarioMasActivo; }
        public String getTareaMasPopular() { return tareaMasPopular; }
        public int getCompletacionesUltimos7Dias() { return completacionesUltimos7Dias; }
        public int getXpGanadoUltimos7Dias() { return xpGanadoUltimos7Dias; }
        public EstadisticasNiveles getNiveles() { return niveles; }
        public EstadisticasStreaks getStreaks() { return streaks; }
    }

    public static class EstadisticasNiveles {
        private final int nivelMinimo;
        private final int nivelMaximo;
        private final double nivelPromedio;
        private final int usuariosNivelMaximo;

        public EstadisticasNiveles(int nivelMinimo, int nivelMaximo, double nivelPromedio, int usuariosNivelMaximo) {
            this.nivelMinimo = nivelMinimo;
            this.nivelMaximo = nivelMaximo;
            this.nivelPromedio = nivelPromedio;
            this.usuariosNivelMaximo = usuariosNivelMaximo;
        }

        public int getNivelMinimo() { return nivelMinimo; }
        public int getNivelMaximo() { return nivelMaximo; }
        public double getNivelPromedio() { return nivelPromedio; }
        public int getUsuariosNivelMaximo() { return usuariosNivelMaximo; }
    }

    public static class EstadisticasStreaks {
        private final int streakMinimo;
        private final int streakMaximo;
        private final double streakPromedio;
        private final int usuariosConStreak;

        public EstadisticasStreaks(int streakMinimo, int streakMaximo, double streakPromedio, int usuariosConStreak) {
            this.streakMinimo = streakMinimo;
            this.streakMaximo = streakMaximo;
            this.streakPromedio = streakPromedio;
            this.usuariosConStreak = usuariosConStreak;
        }

        public int getStreakMinimo() { return streakMinimo; }
        public int getStreakMaximo() { return streakMaximo; }
        public double getStreakPromedio() { return streakPromedio; }
        public int getUsuariosConStreak() { return usuariosConStreak; }
    }

    public static class TopUsuario {
        private final String nombre;
        private final int nivel;
        private final int experiencia;
        private final int streakActual;

        public TopUsuario(String nombre, int nivel, int experiencia, int streakActual) {
            this.nombre = nombre;
            this.nivel = nivel;
            this.experiencia = experiencia;
            this.streakActual = streakActual;
        }

        public String getNombre() { return nombre; }
        public int getNivel() { return nivel; }
        public int getExperiencia() { return experiencia; }
        public int getStreakActual() { return streakActual; }
    }
}