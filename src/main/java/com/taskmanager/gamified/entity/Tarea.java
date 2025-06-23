package com.taskmanager.gamified.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tareas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_tarea", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("NORMAL")
public class Tarea {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DificultadTarea dificultad;
    
    @Column(name = "xp_base", nullable = false)
    private Integer xpBase;
    
    @Column(nullable = false)
    private Boolean repetible = true;
    
    @Column(nullable = false)
    private Boolean activa = true;
    
    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
    
    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HistorialTarea> historialCompletaciones;
    
    public Tarea() {
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public Tarea(String nombre, String descripcion, DificultadTarea dificultad) {
        this();
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.dificultad = dificultad;
        this.xpBase = dificultad.getXpBase();
    }
    
    public Tarea(String nombre, String descripcion, DificultadTarea dificultad, Integer xpPersonalizado) {
        this(nombre, descripcion, dificultad);
        if (dificultad == DificultadTarea.ESPECIAL) {
            this.xpBase = xpPersonalizado;
        }
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public DificultadTarea getDificultad() { return dificultad; }
    public void setDificultad(DificultadTarea dificultad) { 
        this.dificultad = dificultad;
        if (dificultad != DificultadTarea.ESPECIAL) {
            this.xpBase = dificultad.getXpBase();
        }
    }
    
    public Integer getXpBase() { return xpBase; }
    public void setXpBase(Integer xpBase) { this.xpBase = xpBase; }
    
    public Boolean getRepetible() { return repetible; }
    public void setRepetible(Boolean repetible) { this.repetible = repetible; }
    
    public Boolean getActiva() { return activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public List<HistorialTarea> getHistorialCompletaciones() { return historialCompletaciones; }
    public void setHistorialCompletaciones(List<HistorialTarea> historialCompletaciones) { this.historialCompletaciones = historialCompletaciones; }
}