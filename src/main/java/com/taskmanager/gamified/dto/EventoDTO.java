package com.taskmanager.gamified.dto;

import java.time.LocalDateTime;

public class EventoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double multiplicador;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
    
    public EventoDTO() {}
    
    public EventoDTO(Long id, String nombre, String descripcion, Double multiplicador,
                    LocalDateTime fechaInicio, LocalDateTime fechaFin, Boolean activo, 
                    LocalDateTime fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.multiplicador = multiplicador;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Double getMultiplicador() { return multiplicador; }
    public void setMultiplicador(Double multiplicador) { this.multiplicador = multiplicador; }
    
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}