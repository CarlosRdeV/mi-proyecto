package com.taskmanager.gamified.service;

import com.taskmanager.gamified.dto.UsuarioDTO;
import com.taskmanager.gamified.dto.CrearUsuarioRequest;
import com.taskmanager.gamified.entity.Usuario;
import com.taskmanager.gamified.entity.HistorialTarea;
import com.taskmanager.gamified.repository.UsuarioRepository;
import com.taskmanager.gamified.repository.HistorialTareaRepository;
import com.taskmanager.gamified.mapper.UsuarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private HistorialTareaRepository historialTareaRepository;
    
    @Autowired
    private UsuarioMapper usuarioMapper;

    /**
     * Calcula el XP requerido para alcanzar un nivel específico
     * Fórmula: Nivel 1 = 25 XP, escalamiento gradual
     */
    public static int calcularXpParaNivel(int nivel) {
        if (nivel <= 1) return 0;
        
        int xpTotal = 0;
        for (int i = 1; i < nivel; i++) {
            if (i == 1) {
                xpTotal += 25; // Primer nivel requiere 25 XP
            } else if (i <= 5) {
                xpTotal += 25 + (i - 1) * 15; // Escalamiento gradual hasta nivel 5
            } else if (i <= 10) {
                xpTotal += 50 + (i - 5) * 20; // Escalamiento medio hasta nivel 10
            } else {
                xpTotal += 100 + (i - 10) * 30; // Escalamiento alto después del nivel 10
            }
        }
        return xpTotal;
    }

    /**
     * Calcula el nivel basado en la experiencia actual
     */
    public static int calcularNivelDesdeXp(int experiencia) {
        int nivel = 1;
        while (calcularXpParaNivel(nivel + 1) <= experiencia) {
            nivel++;
        }
        return nivel;
    }

    /**
     * Calcula el multiplicador de streak para el día especificado
     * Día 1-4: incremento gradual, Día 5+: multiplicador fijo de 1.25x
     */
    public static double calcularMultiplicadorStreak(int streakDias) {
        if (streakDias <= 0) return 1.0;
        if (streakDias == 1) return 1.05; // 5% extra el primer día
        if (streakDias == 2) return 1.10; // 10% extra el segundo día
        if (streakDias == 3) return 1.15; // 15% extra el tercer día
        if (streakDias == 4) return 1.20; // 20% extra el cuarto día
        return 1.25; // 25% extra del día 5 en adelante
    }

    /**
     * Obtiene todos los usuarios como DTOs
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuarioMapper::toDTO)
                .toList();
    }

    /**
     * Obtiene un usuario por ID
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDTO);
    }

    /**
     * Obtiene un usuario por nombre
     */
    @Transactional(readOnly = true)
    public Optional<UsuarioDTO> obtenerUsuarioPorNombre(String nombre) {
        return usuarioRepository.findByNombre(nombre)
                .map(usuarioMapper::toDTO);
    }

    /**
     * Crea un nuevo usuario
     */
    @Transactional
    public UsuarioDTO crearUsuario(CrearUsuarioRequest request) {
        // Verificar que el nombre no exista
        if (usuarioRepository.findByNombre(request.getNombre()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con el nombre: " + request.getNombre());
        }

        Usuario usuario = new Usuario(request.getNombre());
        usuario = usuarioRepository.save(usuario);
        
        return usuarioMapper.toDTO(usuario);
    }

    /**
     * Actualiza la fecha de último login del usuario
     */
    @Transactional
    public void actualizarUltimoLogin(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));
        
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime ultimoLogin = usuario.getFechaUltimoLogin();
        
        // Verificar si el streak debe continuar o reiniciarse
        if (ultimoLogin != null) {
            LocalDate ultimoDia = ultimoLogin.toLocalDate();
            LocalDate hoy = ahora.toLocalDate();
            
            if (ultimoDia.equals(hoy)) {
                // Mismo día, no hacer nada con el streak
                usuario.setFechaUltimoLogin(ahora);
            } else if (ultimoDia.equals(hoy.minusDays(1))) {
                // Día consecutivo, incrementar streak
                usuario.setStreakActual(usuario.getStreakActual() + 1);
                usuario.setFechaUltimoLogin(ahora);
            } else {
                // Se perdió el streak, reiniciar
                usuario.setStreakActual(1);
                usuario.setFechaUltimoLogin(ahora);
            }
        } else {
            // Primer login
            usuario.setStreakActual(1);
            usuario.setFechaUltimoLogin(ahora);
        }
        
        usuarioRepository.save(usuario);
    }

    /**
     * Agrega experiencia al usuario y actualiza su nivel automáticamente
     */
    @Transactional
    public UsuarioDTO agregarExperiencia(Long usuarioId, int xpGanado) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));

        int xpAnterior = usuario.getExperiencia();
        int nivelAnterior = usuario.getNivel();
        
        // Agregar XP
        usuario.setExperiencia(xpAnterior + xpGanado);
        
        // Recalcular nivel
        int nuevoNivel = calcularNivelDesdeXp(usuario.getExperiencia());
        usuario.setNivel(nuevoNivel);
        
        // Actualizar fecha de última actividad
        usuario.setFechaActualizacion(LocalDateTime.now());
        
        usuario = usuarioRepository.save(usuario);
        
        return usuarioMapper.toDTO(usuario);
    }

    /**
     * Verifica si el usuario ha completado al menos una tarea hoy
     * (para mantener el streak)
     */
    @Transactional(readOnly = true)
    public boolean haCompletadoTareaHoy(Long usuarioId) {
        LocalDateTime inicioDelDia = LocalDate.now().atStartOfDay();
        LocalDateTime finDelDia = inicioDelDia.plusDays(1);
        
        List<HistorialTarea> tareasHoy = historialTareaRepository
                .findByUsuarioIdAndFechaCompletacionBetween(usuarioId, inicioDelDia, finDelDia);
        
        return !tareasHoy.isEmpty();
    }

    /**
     * Obtiene el progreso hacia el siguiente nivel
     */
    @Transactional(readOnly = true)
    public ProgresoNivel obtenerProgresoNivel(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + usuarioId));

        int nivelActual = usuario.getNivel();
        int xpActual = usuario.getExperiencia();
        int xpParaNivelActual = calcularXpParaNivel(nivelActual);
        int xpParaSiguienteNivel = calcularXpParaNivel(nivelActual + 1);
        
        int xpEnNivelActual = xpActual - xpParaNivelActual;
        int xpRequeridoParaSiguiente = xpParaSiguienteNivel - xpParaNivelActual;
        
        double porcentaje = xpRequeridoParaSiguiente > 0 
            ? (double) xpEnNivelActual / xpRequeridoParaSiguiente * 100 
            : 100.0;

        return new ProgresoNivel(
                nivelActual,
                nivelActual + 1,
                xpEnNivelActual,
                xpRequeridoParaSiguiente,
                Math.min(porcentaje, 100.0),
                xpParaSiguienteNivel - xpActual
        );
    }

    /**
     * DTO para representar el progreso de nivel
     */
    public static class ProgresoNivel {
        private final int nivelActual;
        private final int siguienteNivel;
        private final int xpEnNivelActual;
        private final int xpRequeridoParaSiguiente;
        private final double porcentajeProgreso;
        private final int xpFaltante;

        public ProgresoNivel(int nivelActual, int siguienteNivel, int xpEnNivelActual, 
                           int xpRequeridoParaSiguiente, double porcentajeProgreso, int xpFaltante) {
            this.nivelActual = nivelActual;
            this.siguienteNivel = siguienteNivel;
            this.xpEnNivelActual = xpEnNivelActual;
            this.xpRequeridoParaSiguiente = xpRequeridoParaSiguiente;
            this.porcentajeProgreso = porcentajeProgreso;
            this.xpFaltante = xpFaltante;
        }

        public int getNivelActual() { return nivelActual; }
        public int getSiguienteNivel() { return siguienteNivel; }
        public int getXpEnNivelActual() { return xpEnNivelActual; }
        public int getXpRequeridoParaSiguiente() { return xpRequeridoParaSiguiente; }
        public double getPorcentajeProgreso() { return porcentajeProgreso; }
        public int getXpFaltante() { return xpFaltante; }
    }
}