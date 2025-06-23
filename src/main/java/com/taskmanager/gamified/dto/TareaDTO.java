package com.taskmanager.gamified.dto;

import com.taskmanager.gamified.entity.DificultadTarea;
import java.time.LocalDateTime;

public class TareaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private DificultadTarea dificultad;
    private Integer xpBase;
    private Boolean repetible;
    private Boolean activa;
    private LocalDateTime fechaCreacion;
    private String tipoTarea;
    
    // Para tareas especiales
    private LocalDateTime fechaLimite;
    private Boolean unaVezPorUsuario;
    
    public TareaDTO() {}
    
    public TareaDTO(Long id, String nombre, String descripcion, DificultadTarea dificultad,
                   Integer xpBase, Boolean repetible, Boolean activa, LocalDateTime fechaCreacion,
                   String tipoTarea, LocalDateTime fechaLimite, Boolean unaVezPorUsuario) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dificultad = dificultad;
        this.xpBase = xpBase;
        this.repetible = repetible;
        this.activa = activa;
        this.fechaCreacion = fechaCreacion;
        this.tipoTarea = tipoTarea;
        this.fechaLimite = fechaLimite;
        this.unaVezPorUsuario = unaVezPorUsuario;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public DificultadTarea getDificultad() { return dificultad; }
    public void setDificultad(DificultadTarea dificultad) { this.dificultad = dificultad; }
    
    public Integer getXpBase() { return xpBase; }
    public void setXpBase(Integer xpBase) { this.xpBase = xpBase; }
    
    public Boolean getRepetible() { return repetible; }
    public void setRepetible(Boolean repetible) { this.repetible = repetible; }
    
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public String getTipoTarea() { return tipoTarea; }
    public void setTipoTarea(String tipoTarea) { this.tipoTarea = tipoTarea; }
    
    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }
    
    public Boolean getUnaVezPorUsuario() { return unaVezPorUsuario; }
    public void setUnaVezPorUsuario(Boolean unaVezPorUsuario) { this.unaVezPorUsuario = unaVezPorUsuario; }
}