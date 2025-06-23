package com.taskmanager.gamified.integration;

import com.taskmanager.gamified.entity.Tarea;
import com.taskmanager.gamified.entity.DificultadTarea;
import com.taskmanager.gamified.repository.TareaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TareaRepositoryIntegrationTest extends BaseIntegrationTest {
    
    @Autowired
    private TareaRepository tareaRepository;
    
    @BeforeEach
    void setUp() {
        tareaRepository.deleteAll();
        
        // Crear tareas de prueba
        Tarea tarea1 = new Tarea("Tarea Fácil 1", "Descripción 1", DificultadTarea.FACIL);
        Tarea tarea2 = new Tarea("Tarea Media 1", "Descripción 2", DificultadTarea.MEDIA);
        Tarea tarea3 = new Tarea("Tarea Difícil 1", "Descripción 3", DificultadTarea.DIFICIL);
        Tarea tarea4 = new Tarea("Tarea Fácil 2", "Descripción 4", DificultadTarea.FACIL);
        
        // Una tarea inactiva
        Tarea tareaInactiva = new Tarea("Tarea Inactiva", "Descripción inactiva", DificultadTarea.MEDIA);
        tareaInactiva.setActiva(false);
        
        tareaRepository.saveAll(List.of(tarea1, tarea2, tarea3, tarea4, tareaInactiva));
    }
    
    @Test
    void testGuardarYBuscarTarea() {
        Tarea nuevaTarea = new Tarea("Nueva Tarea", "Nueva descripción", DificultadTarea.MEDIA);
        Tarea guardada = tareaRepository.save(nuevaTarea);
        
        assertNotNull(guardada.getId());
        assertEquals("Nueva Tarea", guardada.getNombre());
        assertEquals(DificultadTarea.MEDIA, guardada.getDificultad());
        assertEquals(Integer.valueOf(25), guardada.getXpBase());
    }
    
    @Test
    void testBuscarTareasActivasOrdenadas() {
        List<Tarea> tareasActivas = tareaRepository.findByActivaTrueOrderByDificultadAscNombreAsc();
        
        assertEquals(4, tareasActivas.size()); // No incluye la inactiva
        
        // Verificar orden: FACIL primero, luego MEDIA, luego DIFICIL
        assertEquals(DificultadTarea.FACIL, tareasActivas.get(0).getDificultad());
        assertEquals(DificultadTarea.FACIL, tareasActivas.get(1).getDificultad());
        assertEquals(DificultadTarea.MEDIA, tareasActivas.get(2).getDificultad());
        assertEquals(DificultadTarea.DIFICIL, tareasActivas.get(3).getDificultad());
        
        // Verificar orden alfabético dentro de la misma dificultad
        assertTrue(tareasActivas.get(0).getNombre().compareTo(tareasActivas.get(1).getNombre()) <= 0);
    }
    
    @Test
    void testBuscarPorDificultadYActiva() {
        List<Tarea> tareasFaciles = tareaRepository.findByDificultadAndActivaTrue(DificultadTarea.FACIL);
        List<Tarea> tareasMedias = tareaRepository.findByDificultadAndActivaTrue(DificultadTarea.MEDIA);
        List<Tarea> tareasDificiles = tareaRepository.findByDificultadAndActivaTrue(DificultadTarea.DIFICIL);
        
        assertEquals(2, tareasFaciles.size());
        assertEquals(1, tareasMedias.size()); // No incluye la inactiva
        assertEquals(1, tareasDificiles.size());
    }
    
    @Test
    void testBuscarTareasNormalesActivas() {
        List<Tarea> tareasNormales = tareaRepository.findTareasNormalesActivas();
        
        assertEquals(4, tareasNormales.size());
        
        // Verificar que todas son de tipo normal (no especiales)
        for (Tarea tarea : tareasNormales) {
            assertTrue(tarea.getClass().equals(Tarea.class));
            assertTrue(tarea.getActiva());
        }
    }
    
    @Test
    void testContarPorDificultadYActiva() {
        Long countFacil = tareaRepository.countByDificultadAndActivaTrue(DificultadTarea.FACIL);
        Long countMedia = tareaRepository.countByDificultadAndActivaTrue(DificultadTarea.MEDIA);
        Long countDificil = tareaRepository.countByDificultadAndActivaTrue(DificultadTarea.DIFICIL);
        Long countEspecial = tareaRepository.countByDificultadAndActivaTrue(DificultadTarea.ESPECIAL);
        
        assertEquals(Long.valueOf(2), countFacil);
        assertEquals(Long.valueOf(1), countMedia); // No cuenta la inactiva
        assertEquals(Long.valueOf(1), countDificil);
        assertEquals(Long.valueOf(0), countEspecial);
    }
    
    @Test
    void testActualizarTarea() {
        List<Tarea> tareas = tareaRepository.findByDificultadAndActivaTrue(DificultadTarea.FACIL);
        Tarea tarea = tareas.get(0);
        
        tarea.setNombre("Nombre Actualizado");
        tarea.setDescripcion("Descripción Actualizada");
        
        Tarea actualizada = tareaRepository.save(tarea);
        
        assertEquals("Nombre Actualizado", actualizada.getNombre());
        assertEquals("Descripción Actualizada", actualizada.getDescripcion());
    }
    
    @Test
    void testDesactivarTarea() {
        List<Tarea> tareasAntes = tareaRepository.findByActivaTrueOrderByDificultadAscNombreAsc();
        int countAntes = tareasAntes.size();
        
        Tarea tarea = tareasAntes.get(0);
        tarea.setActiva(false);
        tareaRepository.save(tarea);
        
        List<Tarea> tareasDespues = tareaRepository.findByActivaTrueOrderByDificultadAscNombreAsc();
        assertEquals(countAntes - 1, tareasDespues.size());
    }
}