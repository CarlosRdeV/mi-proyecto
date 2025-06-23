package com.taskmanager.gamified.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    
    private Usuario usuario;
    
    @BeforeEach
    void setUp() {
        usuario = new Usuario("TestUser");
    }
    
    @Test
    void testCreacionUsuario() {
        assertNotNull(usuario);
        assertEquals("TestUser", usuario.getNombre());
        assertEquals(Integer.valueOf(1), usuario.getNivel());
        assertEquals(Integer.valueOf(0), usuario.getExperiencia());
        assertEquals(Integer.valueOf(0), usuario.getStreakActual());
        assertNotNull(usuario.getFechaCreacion());
    }
    
    @Test
    void testUsuarioVacio() {
        Usuario usuarioVacio = new Usuario();
        assertNotNull(usuarioVacio);
        assertEquals(Integer.valueOf(1), usuarioVacio.getNivel());
        assertEquals(Integer.valueOf(0), usuarioVacio.getExperiencia());
        assertEquals(Integer.valueOf(0), usuarioVacio.getStreakActual());
        assertNotNull(usuarioVacio.getFechaCreacion());
    }
    
    @Test
    void testSettersAndGetters() {
        usuario.setNivel(5);
        usuario.setExperiencia(150);
        usuario.setStreakActual(7);
        LocalDateTime fechaLogin = LocalDateTime.now();
        usuario.setFechaUltimoLogin(fechaLogin);
        
        assertEquals(Integer.valueOf(5), usuario.getNivel());
        assertEquals(Integer.valueOf(150), usuario.getExperiencia());
        assertEquals(Integer.valueOf(7), usuario.getStreakActual());
        assertEquals(fechaLogin, usuario.getFechaUltimoLogin());
    }
    
    @Test
    void testPreUpdate() {
        LocalDateTime antes = LocalDateTime.now();
        usuario.preUpdate();
        LocalDateTime despues = LocalDateTime.now();
        
        assertNotNull(usuario.getFechaActualizacion());
        assertTrue(usuario.getFechaActualizacion().isAfter(antes) || 
                  usuario.getFechaActualizacion().isEqual(antes));
        assertTrue(usuario.getFechaActualizacion().isBefore(despues) || 
                  usuario.getFechaActualizacion().isEqual(despues));
    }
}