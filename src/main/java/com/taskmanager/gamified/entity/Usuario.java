package com.taskmanager.gamified.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
    
    @Column(nullable = false)
    private Integer nivel = 1;
    
    @Column(nullable = false)
    private Integer experiencia = 0;
    
    @Column(name = "streak_actual", nullable = false)
    private Integer streakActual = 0;
    
    @Column(name = "fecha_ultimo_login")
    private LocalDateTime fechaUltimoLogin;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HistorialTarea> historialTareas;
    
    public Usuario() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Usuario(String nombre) {
        this();
        this.nombre = nombre;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
    
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
    
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    
    public List<HistorialTarea> getHistorialTareas() { return historialTareas; }
    public void setHistorialTareas(List<HistorialTarea> historialTareas) { this.historialTareas = historialTareas; }
}