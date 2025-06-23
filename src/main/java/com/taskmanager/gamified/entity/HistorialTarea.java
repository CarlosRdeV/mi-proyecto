package com.taskmanager.gamified.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_tareas")
public class HistorialTarea {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarea_id", nullable = false)
    private Tarea tarea;
    
    @Column(name = "fecha_completacion", nullable = false)
    private LocalDateTime fechaCompletacion;
    
    @Column(name = "xp_ganado", nullable = false)
    private Integer xpGanado;
    
    @Column(name = "multiplicador_streak")
    private Double multiplicadorStreak;
    
    @Column(name = "multiplicador_evento")
    private Double multiplicadorEvento;
    
    @Column(name = "multiplicador_total")
    private Double multiplicadorTotal;
    
    public HistorialTarea() {
        this.fechaCompletacion = LocalDateTime.now();
    }
    
    public HistorialTarea(Usuario usuario, Tarea tarea, Integer xpGanado) {
        this();
        this.usuario = usuario;
        this.tarea = tarea;
        this.xpGanado = xpGanado;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    
    public Tarea getTarea() { return tarea; }
    public void setTarea(Tarea tarea) { this.tarea = tarea; }
    
    public LocalDateTime getFechaCompletacion() { return fechaCompletacion; }
    public void setFechaCompletacion(LocalDateTime fechaCompletacion) { this.fechaCompletacion = fechaCompletacion; }
    
    public Integer getXpGanado() { return xpGanado; }
    public void setXpGanado(Integer xpGanado) { this.xpGanado = xpGanado; }
    
    public Double getMultiplicadorStreak() { return multiplicadorStreak; }
    public void setMultiplicadorStreak(Double multiplicadorStreak) { this.multiplicadorStreak = multiplicadorStreak; }
    
    public Double getMultiplicadorEvento() { return multiplicadorEvento; }
    public void setMultiplicadorEvento(Double multiplicadorEvento) { this.multiplicadorEvento = multiplicadorEvento; }
    
    public Double getMultiplicadorTotal() { return multiplicadorTotal; }
    public void setMultiplicadorTotal(Double multiplicadorTotal) { this.multiplicadorTotal = multiplicadorTotal; }
}