package com.taskmanager.gamified.integration;

import com.taskmanager.gamified.entity.Usuario;
import com.taskmanager.gamified.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioRepositoryIntegrationTest extends BaseIntegrationTest {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    private Usuario usuario1;
    private Usuario usuario2;
    private Usuario usuario3;
    
    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();
        
        usuario1 = new Usuario("Usuario1");
        usuario1.setNivel(3);
        usuario1.setExperiencia(150);
        
        usuario2 = new Usuario("Usuario2");
        usuario2.setNivel(3);
        usuario2.setExperiencia(200);
        
        usuario3 = new Usuario("Usuario3");
        usuario3.setNivel(5);
        usuario3.setExperiencia(300);
        
        usuarioRepository.saveAll(List.of(usuario1, usuario2, usuario3));
    }
    
    @Test
    void testGuardarYBuscarUsuario() {
        Usuario nuevoUsuario = new Usuario("NuevoUsuario");
        Usuario guardado = usuarioRepository.save(nuevoUsuario);
        
        assertNotNull(guardado.getId());
        assertEquals("NuevoUsuario", guardado.getNombre());
        
        Optional<Usuario> encontrado = usuarioRepository.findById(guardado.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("NuevoUsuario", encontrado.get().getNombre());
    }
    
    @Test
    void testBuscarPorNombre() {
        Optional<Usuario> encontrado = usuarioRepository.findByNombre("Usuario1");
        assertTrue(encontrado.isPresent());
        assertEquals("Usuario1", encontrado.get().getNombre());
        assertEquals(Integer.valueOf(3), encontrado.get().getNivel());
        assertEquals(Integer.valueOf(150), encontrado.get().getExperiencia());
    }
    
    @Test
    void testExistePorNombre() {
        assertTrue(usuarioRepository.existsByNombre("Usuario1"));
        assertFalse(usuarioRepository.existsByNombre("UsuarioInexistente"));
    }
    
    @Test
    void testBuscarPorNivelOrdenadoPorExperiencia() {
        List<Usuario> usuariosNivel3 = usuarioRepository.findByNivelOrderByExperienciaDesc(3);
        
        assertEquals(2, usuariosNivel3.size());
        assertEquals("Usuario2", usuariosNivel3.get(0).getNombre()); // 200 XP
        assertEquals("Usuario1", usuariosNivel3.get(1).getNombre()); // 150 XP
    }
    
    @Test
    void testBuscarTodosOrdenadosPorNivelYExperiencia() {
        List<Usuario> usuarios = usuarioRepository.findAllOrderByNivelAndExperiencia();
        
        assertEquals(3, usuarios.size());
        assertEquals("Usuario3", usuarios.get(0).getNombre()); // Nivel 5, 300 XP
        assertEquals("Usuario2", usuarios.get(1).getNombre()); // Nivel 3, 200 XP
        assertEquals("Usuario1", usuarios.get(2).getNombre()); // Nivel 3, 150 XP
    }
    
    @Test
    void testNombreUnico() {
        Usuario usuarioDuplicado = new Usuario("Usuario1");
        
        assertThrows(DataIntegrityViolationException.class, () -> {
            usuarioRepository.saveAndFlush(usuarioDuplicado);
        });
    }
    
    @Test
    void testActualizarUsuario() {
        Usuario usuario = usuarioRepository.findByNombre("Usuario1").orElseThrow();
        usuario.setNivel(4);
        usuario.setExperiencia(175);
        usuario.setStreakActual(5);
        
        Usuario actualizado = usuarioRepository.save(usuario);
        
        assertEquals(Integer.valueOf(4), actualizado.getNivel());
        assertEquals(Integer.valueOf(175), actualizado.getExperiencia());
        assertEquals(Integer.valueOf(5), actualizado.getStreakActual());
    }
    
    @Test
    void testEliminarUsuario() {
        Long userId = usuario1.getId();
        usuarioRepository.delete(usuario1);
        
        Optional<Usuario> eliminado = usuarioRepository.findById(userId);
        assertFalse(eliminado.isPresent());
        
        assertEquals(2, usuarioRepository.count());
    }
}