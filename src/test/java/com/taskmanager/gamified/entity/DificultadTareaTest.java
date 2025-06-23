package com.taskmanager.gamified.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DificultadTareaTest {
    
    @Test
    void testValoresXpBase() {
        assertEquals(10, DificultadTarea.FACIL.getXpBase());
        assertEquals(25, DificultadTarea.MEDIA.getXpBase());
        assertEquals(50, DificultadTarea.DIFICIL.getXpBase());
        assertEquals(0, DificultadTarea.ESPECIAL.getXpBase());
    }
    
    @Test
    void testTodosLosValores() {
        DificultadTarea[] valores = DificultadTarea.values();
        assertEquals(4, valores.length);
        
        boolean facilEncontrado = false;
        boolean mediaEncontrado = false;
        boolean dificilEncontrado = false;
        boolean especialEncontrado = false;
        
        for (DificultadTarea dificultad : valores) {
            switch (dificultad) {
                case FACIL -> facilEncontrado = true;
                case MEDIA -> mediaEncontrado = true;
                case DIFICIL -> dificilEncontrado = true;
                case ESPECIAL -> especialEncontrado = true;
            }
        }
        
        assertTrue(facilEncontrado);
        assertTrue(mediaEncontrado);
        assertTrue(dificilEncontrado);
        assertTrue(especialEncontrado);
    }
    
    @Test
    void testConversionDesdeString() {
        assertEquals(DificultadTarea.FACIL, DificultadTarea.valueOf("FACIL"));
        assertEquals(DificultadTarea.MEDIA, DificultadTarea.valueOf("MEDIA"));
        assertEquals(DificultadTarea.DIFICIL, DificultadTarea.valueOf("DIFICIL"));
        assertEquals(DificultadTarea.ESPECIAL, DificultadTarea.valueOf("ESPECIAL"));
    }
}