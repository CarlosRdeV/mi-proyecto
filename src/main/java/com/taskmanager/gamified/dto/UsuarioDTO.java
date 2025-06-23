package com.taskmanager.gamified.dto;

import java.time.LocalDateTime;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    private Integer nivel;
    private Integer experiencia;
    private Integer streakActual;
    private LocalDateTime fechaUltimoLogin;
    private LocalDateTime fechaCreacion;
    
    public UsuarioDTO() {}
    
    public UsuarioDTO(Long id, String nombre, Integer nivel, Integer experiencia, 
                     Integer streakActual, LocalDateTime fechaUltimoLogin, LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.streakActual = streakActual;
        this.fechaUltimoLogin = fechaUltimoLogin;
        this.fechaCreacion = fechaCreacion;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }
    
    public Integer getExperiencia() { return experiencia; }
    public void setExperiencia(Integer experiencia) { this.experiencia = experiencia; }
    
    public Integer getStreakActual() { return streakActual; }
    public void setStreakActual(Integer streakActual) { this.streakActual = streakActual; }
    
    public LocalDateTime getFechaUltimoLogin() { return fechaUltimoLogin; }
    public void setFechaUltimoLogin(LocalDateTime fechaUltimoLogin) { this.fechaUltimoLogin = fechaUltimoLogin; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}