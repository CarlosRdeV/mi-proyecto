# DTOs - Data Transfer Objects

## Propósito

Los DTOs en este proyecto sirven para:
- **Separación de capas**: Aislar la capa de presentación de las entidades JPA
- **Seguridad**: Evitar exposición accidental de datos sensibles o relaciones lazy
- **Versionado de API**: Mantener estabilidad en la API independiente de cambios en entidades
- **Validación**: Aplicar validaciones específicas en requests
- **Performance**: Evitar problemas de N+1 queries y lazy loading

## DTOs Implementados

### UsuarioDTO
```java
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private Integer nivel;
    private Integer experiencia;
    private Integer streakActual;
    private LocalDateTime fechaUltimoLogin;
    private LocalDateTime fechaCreacion;
}
```
**Uso**: Respuestas de API que requieren información del usuario sin relaciones complejas.

### TareaDTO
```java
public class TareaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private DificultadTarea dificultad;
    private Integer xpBase;
    private Boolean repetible;
    private Boolean activa;
    private LocalDateTime fechaCreacion;
    private String tipoTarea;            // "NORMAL" o "ESPECIAL"
    private LocalDateTime fechaLimite;   // Solo para tareas especiales
    private Boolean unaVezPorUsuario;    // Solo para tareas especiales
}
```
**Uso**: Lista de tareas disponibles. El campo `tipoTarea` permite distinguir entre tareas normales y especiales.

### EventoDTO
```java
public class EventoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double multiplicador;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
```
**Uso**: Información de eventos multiplicadores activos.

### EstadisticasDTO
```java
public class EstadisticasDTO {
    private Long totalUsuarios;
    private Long totalTareas;
    private Long totalEventos;
    private Long tareasActivas;
    private Long eventosVigentes;
    private LocalDateTime timestamp;
}
```
**Uso**: Dashboard con contadores del sistema. Incluye timestamp para cache.

### CrearUsuarioRequest
```java
public class CrearUsuarioRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 1, max = 50, message = "El nombre debe tener entre 1 y 50 caracteres")
    private String nombre;
}
```
**Uso**: Request para crear usuarios con validaciones Bean Validation.

## Mappers

### UsuarioMapper
```java
@Component
public class UsuarioMapper {
    public UsuarioDTO toDTO(Usuario usuario);
    public List<UsuarioDTO> toDTO(List<Usuario> usuarios);
    public Usuario toEntity(UsuarioDTO dto);
}
```

### TareaMapper
```java
@Component
public class TareaMapper {
    public TareaDTO toDTO(Tarea tarea);
    public List<TareaDTO> toDTO(List<Tarea> tareas);
    // Maneja herencia Tarea/TareaEspecial automáticamente
}
```

### EventoMapper
```java
@Component
public class EventoMapper {
    public EventoDTO toDTO(Evento evento);
    public List<EventoDTO> toDTO(List<Evento> eventos);
}
```

## Buenas Prácticas

### 1. Nunca exponer entidades directamente
```java
// ❌ MAL - Expone entidad directamente
@GetMapping("/usuarios")
public List<Usuario> obtenerUsuarios() {
    return usuarioRepository.findAll();
}

// ✅ BIEN - Usa DTO
@GetMapping("/usuarios")  
public List<UsuarioDTO> obtenerUsuarios() {
    return usuarioMapper.toDTO(usuarioRepository.findAll());
}
```

### 2. Validaciones en DTOs de request
```java
// ✅ BIEN - Validaciones en DTO
@PostMapping("/usuarios")
public ResponseEntity<?> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
    // ...
}
```

### 3. Manejar errores en controladores
```java
// ✅ BIEN - ResponseEntity para manejo de errores
@PostMapping("/usuarios")
public ResponseEntity<?> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
    try {
        // lógica...
        return ResponseEntity.ok(usuarioDTO);
    } catch (Exception e) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", e.getMessage()));
    }
}
```

### 4. DTOs específicos por uso
```java
// ✅ BIEN - DTOs específicos
public class UsuarioPerfilDTO { /* campos básicos */ }
public class UsuarioEstadisticasDTO { /* campos + stats */ }
public class CrearUsuarioRequest { /* solo campos requeridos */ }
```

## Ventajas de esta Implementación

1. **Lazy Loading Seguro**: Sin @JsonIgnore, los DTOs evitan problemas de serialización
2. **API Estable**: Cambios en entidades no afectan la API
3. **Validación Centralizada**: Bean Validation en DTOs de request
4. **Performance**: Sin queries N+1 por relaciones lazy
5. **Seguridad**: Control total sobre qué datos se exponen
6. **Testing**: DTOs facilitan tests de controlador MockMvc
7. **Documentación**: DTOs actúan como contrato de API claro

## Evolución Futura

Cuando se implementen más funcionalidades:

1. **Paginación**: Crear `PagedResultDTO<T>`
2. **Filtros**: DTOs para criterios de búsqueda
3. **Requests complejos**: DTOs para operaciones batch
4. **Responses detallados**: DTOs con datos calculados/agregados
5. **Versionado**: DTOs v1, v2 para compatibilidad hacia atrás