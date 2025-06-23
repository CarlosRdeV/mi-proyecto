package com.taskmanager.gamified.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventoTest {
    
    private Evento evento;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    
    @BeforeEach
    void setUp() {
        fechaInicio = LocalDateTime.now().minusDays(1);
        fechaFin = LocalDateTime.now().plusDays(7);
        evento = new Evento("Evento de Prueba", "Descripción del evento", 1.5, fechaInicio, fechaFin);
    }
    
    @Test
    void testCreacionEvento() {
        assertNotNull(evento);
        assertEquals("Evento de Prueba", evento.getNombre());
        assertEquals("Descripción del evento", evento.getDescripcion());
        assertEquals(Double.valueOf(1.5), evento.getMultiplicador());
        assertEquals(fechaInicio, evento.getFechaInicio());
        assertEquals(fechaFin, evento.getFechaFin());
        assertTrue(evento.getActivo());
        assertNotNull(evento.getFechaCreacion());
    }
    
    @Test
    void testEventoVacio() {
        Evento eventoVacio = new Evento();
        assertNotNull(eventoVacio);
        assertTrue(eventoVacio.getActivo());
        assertNotNull(eventoVacio.getFechaCreacion());
    }
    
    @Test
    void testEstaVigenteEventoActivo() {
        assertTrue(evento.estaVigente());
    }
    
    @Test
    void testEstaVigenteEventoInactivo() {
        evento.setActivo(false);
        assertFalse(evento.estaVigente());
    }
    
    @Test
    void testEstaVigenteAntesDeFechaInicio() {
        LocalDateTime futuro = LocalDateTime.now().plusDays(10);
        evento.setFechaInicio(futuro);
        evento.setFechaFin(futuro.plusDays(7));
        assertFalse(evento.estaVigente());
    }
    
    @Test
    void testEstaVigenteDespuesDeFechaFin() {
        LocalDateTime pasado = LocalDateTime.now().minusDays(10);
        evento.setFechaInicio(pasado.minusDays(7));
        evento.setFechaFin(pasado);
        assertFalse(evento.estaVigente());
    }
    
    @Test
    void testSettersAndGetters() {
        evento.setNombre("Nuevo Nombre");
        evento.setDescripcion("Nueva Descripción");
        evento.setMultiplicador(2.0);
        evento.setActivo(false);
        
        assertEquals("Nuevo Nombre", evento.getNombre());
        assertEquals("Nueva Descripción", evento.getDescripcion());
        assertEquals(Double.valueOf(2.0), evento.getMultiplicador());
        assertFalse(evento.getActivo());
    }
}