package com.taskmanager.gamified.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import com.taskmanager.gamified.dto.CrearUsuarioRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TestControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testHealthEndpoint() throws Exception {
        mockMvc.perform(get("/api/test/health"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status", is("OK")))
                .andExpect(jsonPath("$.message", is("Task Manager Gamificado funcionando correctamente")))
                .andExpect(jsonPath("$.timestamp", notNullValue()));
    }
    
    @Test
    void testObtenerUsuarios() throws Exception {
        mockMvc.perform(get("/api/test/usuarios"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }
    
    @Test
    void testObtenerTareas() throws Exception {
        mockMvc.perform(get("/api/test/tareas"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)))
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }
    
    @Test
    void testObtenerEventos() throws Exception {
        mockMvc.perform(get("/api/test/eventos"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", isA(java.util.List.class)));
    }
    
    @Test
    void testObtenerEstadisticas() throws Exception {
        mockMvc.perform(get("/api/test/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalUsuarios", isA(Number.class)))
                .andExpect(jsonPath("$.totalTareas", isA(Number.class)))
                .andExpect(jsonPath("$.totalEventos", isA(Number.class)))
                .andExpect(jsonPath("$.tareasActivas", isA(Number.class)))
                .andExpect(jsonPath("$.eventosVigentes", isA(Number.class)));
    }
    
    @Test
    void testCrearUsuario() throws Exception {
        CrearUsuarioRequest usuarioRequest = new CrearUsuarioRequest("UsuarioTest");
        String jsonRequest = objectMapper.writeValueAsString(usuarioRequest);
        
        mockMvc.perform(post("/api/test/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("UsuarioTest")))
                .andExpect(jsonPath("$.nivel", is(1)))
                .andExpect(jsonPath("$.experiencia", is(0)))
                .andExpect(jsonPath("$.streakActual", is(0)))
                .andExpect(jsonPath("$.id", notNullValue()));
    }
    
    @Test
    void testCrearUsuarioSinNombre() throws Exception {
        String jsonRequest = "{}";
        
        mockMvc.perform(post("/api/test/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCrearUsuarioConNombreVacio() throws Exception {
        CrearUsuarioRequest usuarioRequest = new CrearUsuarioRequest("");
        String jsonRequest = objectMapper.writeValueAsString(usuarioRequest);
        
        mockMvc.perform(post("/api/test/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCrearUsuarioDuplicado() throws Exception {
        // Crear primer usuario
        CrearUsuarioRequest usuarioRequest1 = new CrearUsuarioRequest("UsuarioDuplicado");
        String jsonRequest1 = objectMapper.writeValueAsString(usuarioRequest1);
        
        mockMvc.perform(post("/api/test/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest1))
                .andExpect(status().isOk());
        
        // Intentar crear usuario duplicado
        CrearUsuarioRequest usuarioRequest2 = new CrearUsuarioRequest("UsuarioDuplicado");
        String jsonRequest2 = objectMapper.writeValueAsString(usuarioRequest2);
        
        mockMvc.perform(post("/api/test/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Ya existe un usuario con ese nombre")));
    }
}