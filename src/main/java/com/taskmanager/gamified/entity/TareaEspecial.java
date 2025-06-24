package com.taskmanager.gamified.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("ESPECIAL")
public class TareaEspecial extends Tarea {
    
    @Column(name = "fecha_limite")
    private LocalDateTime fechaLimite;
    
    @Column(name = "una_vez_por_usuario")
    private Boolean unaVezPorUsuario = true;
    
    public TareaEspecial() {
        super();
        this.setRepetible(false);
    }
    
    public TareaEspecial(String nombre, String descripcion, Integer xpPersonalizado, LocalDateTime fechaLimite) {
        super(nombre, descripcion, DificultadTarea.ESPECIAL, xpPersonalizado);
        this.fechaLimite = fechaLimite;
        this.setRepetible(false);
    }
    
    public LocalDateTime getFechaLimite() { return fechaLimite; }
    public void setFechaLimite(LocalDateTime fechaLimite) { this.fechaLimite = fechaLimite; }
    
    public Boolean getUnaVezPorUsuario() { return unaVezPorUsuario; }
    public void setUnaVezPorUsuario(Boolean unaVezPorUsuario) { this.unaVezPorUsuario = unaVezPorUsuario; }
    
    public boolean estaVigente() {
        return fechaLimite == null || LocalDateTime.now().isBefore(fechaLimite);
    }
    
    public Integer getXpPersonalizado() {
        return super.getXpBase();
    }
    
    public void setXpPersonalizado(Integer xpPersonalizado) {
        super.setXpBase(xpPersonalizado);
    }
}