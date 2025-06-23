package com.taskmanager.gamified.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos")
public class Evento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(nullable = false)
    private Double multiplicador;
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;
    
    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;
    
    @Column(nullable = false)
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    public Evento() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Evento(String nombre, String descripcion, Double multiplicador, 
                  LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.multiplicador = multiplicador;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    public boolean estaVigente() {
        LocalDateTime ahora = LocalDateTime.now();
        return activo && ahora.isAfter(fechaInicio) && ahora.isBefore(fechaFin);
    }
    
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