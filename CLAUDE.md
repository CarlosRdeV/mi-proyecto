# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Configuración de Idioma

**IMPORTANTE**: Todas las interacciones deben realizarse en español por defecto.

## Descripción del Proyecto

**Task Manager Gamificado** - API RESTful desarrollada con Java Spring Boot que convierte la gestión de tareas en una experiencia de videojuego, donde los usuarios ganan experiencia, suben de nivel y mantienen streaks por completar tareas.

## Tecnologías Utilizadas

- **Backend**: Java 17, Spring Boot 3.2.0
- **Base de Datos**: H2 (desarrollo local)
- **Persistencia**: Spring Data JPA
- **Build Tool**: Maven
- **Autenticación**: Simple ID (preparado para OAuth2 futuro)

## Sistema de Gamificación

### Experiencia y Niveles
- **Nivel 1**: Requiere 25 XP
- **Progresión**: Escalamiento gradual (fácil al inicio, más difícil después)
- **XP por Dificultad**:
  - Tareas Fáciles: 10 XP
  - Tareas Medias: 25 XP  
  - Tareas Difíciles: 50 XP
  - Tareas Especiales: XP personalizable

### Sistema de Streaks
- **Multiplicador gradual**: Aumenta día a día hasta llegar al día 5
- **Día 5**: 1.25x multiplicador de experiencia
- **Acumulativo**: Se suma con eventos multiplicadores

### Eventos Multiplicadores
- **Evento Inicial**: 10% extra (1.1x)
- **Combinación**: Eventos + Streaks se multiplican
- **Ejemplo**: Día 5 + Evento 10% = 1.1 × 1.25 = 1.375x XP

## Catálogo de Tareas

### Tareas Fáciles (10 XP) - Repetibles diariamente
- Beber un vaso de agua
- Dar un paseo de 5 minutos
- Pasear al perro
- Hacer la cama
- Lavar los platos después de comer
- Leer 10 minutos
- Escuchar una canción completa mientras haces ejercicio ligero

### Tareas Medias (25 XP) - Repetibles diariamente
- Hacer ejercicio 30 minutos
- Cocinar una comida completa
- Limpiar una habitación de la casa
- Estudiar/trabajar 1 hora concentrado
- Llamar a un familiar o amigo
- Organizar un espacio de trabajo
- Meditar 15 minutos

### Tareas Difíciles (50 XP) - Repetibles diariamente
- Completar un proyecto importante del trabajo/estudio
- Hacer ejercicio intenso por 1 hora
- Limpiar toda la casa
- Aprender algo nuevo por 2+ horas
- Completar una tarea que has estado posponiendo
- Hacer una actividad social que te saque de tu zona de confort

### Tareas Especiales
- XP personalizable
- Pueden tener fecha límite
- Una sola vez por usuario
- Creadas para eventos específicos

## Funcionalidades del Sistema

### Gestión de Usuarios
- Login simple por ID (sin contraseña por ahora)
- Perfil con nivel, XP actual, streak actual
- Preparado para migración a OAuth2

### Estadísticas de Usuario
- Nivel actual del usuario
- Streak actual (días consecutivos)
- Tarea que más ha cumplido
- Historial de completación (últimos 12 meses)
- Progreso hacia siguiente nivel

### Gestión de Tareas
- Creación, edición y eliminación de tareas
- Completar tareas con cálculo automático de XP
- Tareas repetibles (límite 1 por día)
- Tareas especiales con fecha límite

### Sistema de Eventos
- Eventos multiplicadores temporales
- Fechas de inicio y fin configurables
- Aplicación automática a todas las tareas completadas

## Arquitectura de la Base de Datos

### Entidades Principales
- **Usuario**: ID, nombre, nivel, XP, streak actual, fecha último login
- **Tarea**: ID, nombre, descripción, dificultad, XP base, repetible, activa
- **TareaEspecial**: Hereda de Tarea, incluye fecha límite, completada por usuario
- **HistorialTarea**: Usuario, tarea, fecha completación, XP ganado
- **Evento**: ID, nombre, multiplicador, fecha inicio, fecha fin, activo
- **EstadisticaUsuario**: Usuario, estadísticas calculadas

### Reglas de Negocio
- Historial se mantiene solo por 12 meses
- Tareas repetibles: máximo 1 por día por usuario
- Streaks se pierden si no se completa al menos 1 tarea por día
- XP final = XP base × multiplicador streak × multiplicador evento

## Comandos de Desarrollo

```bash
# Compilar proyecto
mvn clean compile

# Ejecutar aplicación
mvn spring-boot:run

# Ejecutar tests
mvn test

# Generar JAR
mvn clean package
```

## Endpoints Principales

### Usuarios
- `GET /api/users/{id}` - Obtener perfil de usuario
- `POST /api/users` - Crear usuario
- `GET /api/users/{id}/stats` - Estadísticas del usuario

### Tareas
- `GET /api/tasks` - Obtener todas las tareas disponibles
- `POST /api/tasks/{id}/complete` - Completar tarea
- `GET /api/users/{userId}/history` - Historial de tareas completadas

### Eventos
- `GET /api/events/active` - Eventos activos
- `POST /api/events` - Crear evento (admin)

## Frontend

Ver archivo **FRONTEND_SPECS.md** para especificaciones completas del frontend web.

### Stack Frontend Propuesto
- React 18 + TypeScript
- Tailwind CSS + Headless UI (tema retro personalizado)
- TanStack Query + Zustand
- Vite como build tool
- **Tema Visual**: Estilo retro gaming 8-bit/16-bit con efectos neón

### Sincronización Frontend-Backend
- Mantener tipos TypeScript sincronizados con entidades backend
- Versioning paralelo (backend v1.0.0 ↔ frontend v1.0.0)
- Actualizar FRONTEND_SPECS.md cada vez que se modifique la API

## Escalabilidad Futura

El proyecto está diseñado para empezar como sistema individual pero puede escalarse para múltiples usuarios:
- Preparado para OAuth2
- Base de datos puede migrar a PostgreSQL/MySQL
- Posible implementación de rankings globales
- Sistema de logros expandible
- Frontend web responsive y PWA capabilities