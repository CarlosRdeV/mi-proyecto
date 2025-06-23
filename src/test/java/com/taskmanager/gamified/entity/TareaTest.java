package com.taskmanager.gamified.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class TareaTest {
    
    private Tarea tarea;
    
    @BeforeEach
    void setUp() {
        tarea = new Tarea("Tarea de prueba", "Descripción de prueba", DificultadTarea.FACIL);
    }
    
    @Test
    void testCreacionTareaNormal() {
        assertNotNull(tarea);
        assertEquals("Tarea de prueba", tarea.getNombre());
        assertEquals("Descripción de prueba", tarea.getDescripcion());
        assertEquals(DificultadTarea.FACIL, tarea.getDificultad());
        assertEquals(Integer.valueOf(10), tarea.getXpBase());
        assertTrue(tarea.getRepetible());
        assertTrue(tarea.getActiva());
        assertNotNull(tarea.getFechaCreacion());
    }
    
    @Test
    void testXpBasePorDificultad() {
        Tarea tareaFacil = new Tarea("Fácil", "Desc", DificultadTarea.FACIL);
        Tarea tareaMedia = new Tarea("Media", "Desc", DificultadTarea.MEDIA);
        Tarea tareaDificil = new Tarea("Difícil", "Desc", DificultadTarea.DIFICIL);
        
        assertEquals(Integer.valueOf(10), tareaFacil.getXpBase());
        assertEquals(Integer.valueOf(25), tareaMedia.getXpBase());
        assertEquals(Integer.valueOf(50), tareaDificil.getXpBase());
    }
    
    @Test
    void testTareaEspecialConXpPersonalizado() {
        Tarea tareaEspecial = new Tarea("Especial", "Desc", DificultadTarea.ESPECIAL, 75);
        
        assertEquals(DificultadTarea.ESPECIAL, tareaEspecial.getDificultad());
        assertEquals(Integer.valueOf(75), tareaEspecial.getXpBase());
    }
    
    @Test
    void testCambioDificultadActualizaXp() {
        tarea.setDificultad(DificultadTarea.DIFICIL);
        assertEquals(Integer.valueOf(50), tarea.getXpBase());
        
        tarea.setDificultad(DificultadTarea.MEDIA);
        assertEquals(Integer.valueOf(25), tarea.getXpBase());
    }
    
    @Test
    void testCambioDificultadEspecialNoActualizaXp() {
        tarea.setXpBase(100);
        tarea.setDificultad(DificultadTarea.ESPECIAL);
        assertEquals(Integer.valueOf(100), tarea.getXpBase()); // No debe cambiar
    }
    
    @Test
    void testSettersAndGetters() {
        tarea.setRepetible(false);
        tarea.setActiva(false);
        
        assertFalse(tarea.getRepetible());
        assertFalse(tarea.getActiva());
    }
}