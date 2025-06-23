package com.taskmanager.gamified.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CrearUsuarioRequest {
    
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
    private String nombre;
    
    public CrearUsuarioRequest() {}
    
    public CrearUsuarioRequest(String nombre) {
        this.nombre = nombre;
    }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}