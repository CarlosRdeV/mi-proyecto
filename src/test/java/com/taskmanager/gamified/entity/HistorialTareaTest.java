package com.taskmanager.gamified.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class HistorialTareaTest {
    
    private HistorialTarea historial;
    private Usuario usuario;
    private Tarea tarea;
    
    @BeforeEach
    void setUp() {
        usuario = new Usuario("TestUser");
        tarea = new Tarea("Test Task", "Test Description", DificultadTarea.FACIL);
        historial = new HistorialTarea(usuario, tarea, 15);
    }
    
    @Test
    void testCreacionHistorial() {
        assertNotNull(historial);
        assertEquals(usuario, historial.getUsuario());
        assertEquals(tarea, historial.getTarea());
        assertEquals(Integer.valueOf(15), historial.getXpGanado());
        assertNotNull(historial.getFechaCompletacion());
    }
    
    @Test
    void testHistorialVacio() {
        HistorialTarea historialVacio = new HistorialTarea();
        assertNotNull(historialVacio);
        assertNotNull(historialVacio.getFechaCompletacion());
    }
    
    @Test
    void testFechaCompletacionAutomatica() {
        LocalDateTime antes = LocalDateTime.now().minusSeconds(1);
        HistorialTarea nuevoHistorial = new HistorialTarea(usuario, tarea, 25);
        LocalDateTime despues = LocalDateTime.now().plusSeconds(1);
        
        assertTrue(nuevoHistorial.getFechaCompletacion().isAfter(antes));
        assertTrue(nuevoHistorial.getFechaCompletacion().isBefore(despues));
    }
    
    @Test
    void testSettersAndGetters() {
        historial.setMultiplicadorStreak(1.25);
        historial.setMultiplicadorEvento(1.1);
        historial.setMultiplicadorTotal(1.375);
        
        assertEquals(Double.valueOf(1.25), historial.getMultiplicadorStreak());
        assertEquals(Double.valueOf(1.1), historial.getMultiplicadorEvento());
        assertEquals(Double.valueOf(1.375), historial.getMultiplicadorTotal());
    }
    
    @Test
    void testCambioXpGanado() {
        historial.setXpGanado(30);
        assertEquals(Integer.valueOf(30), historial.getXpGanado());
    }
    
    @Test
    void testCambioFechaCompletacion() {
        LocalDateTime nuevaFecha = LocalDateTime.now().minusHours(2);
        historial.setFechaCompletacion(nuevaFecha);
        assertEquals(nuevaFecha, historial.getFechaCompletacion());
    }
}