package com.taskmanager.gamified.dto;

import java.time.LocalDateTime;

public class EstadisticasDTO {
    private Long totalUsuarios;
    private Long totalTareas;
    private Long totalEventos;
    private Long tareasActivas;
    private Long eventosVigentes;
    private LocalDateTime timestamp;
    
    public EstadisticasDTO() {
        this.timestamp = LocalDateTime.now();
    }
    
    public EstadisticasDTO(Long totalUsuarios, Long totalTareas, Long totalEventos,
                          Long totalCompletaciones, LocalDateTime timestamp) {
        this.totalUsuarios = totalUsuarios;
        this.totalTareas = totalTareas;
        this.totalEventos = totalEventos;
        this.tareasActivas = totalCompletaciones;
        this.eventosVigentes = 0L;
        this.timestamp = timestamp;
    }
    
    public Long getTotalCompletaciones() { return tareasActivas; }
    
    // Getters y Setters
    public Long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(Long totalUsuarios) { this.totalUsuarios = totalUsuarios; }
    
    public Long getTotalTareas() { return totalTareas; }
    public void setTotalTareas(Long totalTareas) { this.totalTareas = totalTareas; }
    
    public Long getTotalEventos() { return totalEventos; }
    public void setTotalEventos(Long totalEventos) { this.totalEventos = totalEventos; }
    
    public Long getTareasActivas() { return tareasActivas; }
    public void setTareasActivas(Long tareasActivas) { this.tareasActivas = tareasActivas; }
    
    public Long getEventosVigentes() { return eventosVigentes; }
    public void setEventosVigentes(Long eventosVigentes) { this.eventosVigentes = eventosVigentes; }
    
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}