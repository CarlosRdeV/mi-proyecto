package com.taskmanager.gamified.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TareaEspecialTest {
    
    private TareaEspecial tareaEspecial;
    private LocalDateTime fechaFutura;
    private LocalDateTime fechaPasada;
    
    @BeforeEach
    void setUp() {
        fechaFutura = LocalDateTime.now().plusDays(7);
        fechaPasada = LocalDateTime.now().minusDays(1);
        tareaEspecial = new TareaEspecial("Tarea Especial", "Descripción especial", 100, fechaFutura);
    }
    
    @Test
    void testCreacionTareaEspecial() {
        assertNotNull(tareaEspecial);
        assertEquals("Tarea Especial", tareaEspecial.getNombre());
        assertEquals("Descripción especial", tareaEspecial.getDescripcion());
        assertEquals(DificultadTarea.ESPECIAL, tareaEspecial.getDificultad());
        assertEquals(Integer.valueOf(100), tareaEspecial.getXpBase());
        assertFalse(tareaEspecial.getRepetible()); // Tareas especiales no son repetibles
        assertTrue(tareaEspecial.getUnaVezPorUsuario());
        assertEquals(fechaFutura, tareaEspecial.getFechaLimite());
    }
    
    @Test
    void testTareaEspecialVacia() {
        TareaEspecial tareaVacia = new TareaEspecial();
        assertNotNull(tareaVacia);
        assertFalse(tareaVacia.getRepetible());
        assertTrue(tareaVacia.getUnaVezPorUsuario());
    }
    
    @Test
    void testEstaVigenteConFechaFutura() {
        assertTrue(tareaEspecial.estaVigente());
    }
    
    @Test
    void testEstaVigenteConFechaPasada() {
        tareaEspecial.setFechaLimite(fechaPasada);
        assertFalse(tareaEspecial.estaVigente());
    }
    
    @Test
    void testEstaVigenteSinFechaLimite() {
        tareaEspecial.setFechaLimite(null);
        assertTrue(tareaEspecial.estaVigente());
    }
    
    @Test
    void testSettersAndGetters() {
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(14);
        tareaEspecial.setFechaLimite(nuevaFecha);
        tareaEspecial.setUnaVezPorUsuario(false);
        
        assertEquals(nuevaFecha, tareaEspecial.getFechaLimite());
        assertFalse(tareaEspecial.getUnaVezPorUsuario());
    }
}