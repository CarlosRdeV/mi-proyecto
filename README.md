# Task Manager Gamificado 🎮📋
**Versión v1.3.0** - *Sistema completamente funcional*

API RESTful que convierte la gestión de tareas en una experiencia de videojuego. Los usuarios ganan experiencia (XP), suben de nivel y mantienen rachas (streaks) completando tareas diarias.

## 🚀 Características Principales

- **Sistema de Experiencia**: Gana XP completando tareas según su dificultad
- **Niveles Progresivos**: Sistema escalable que se vuelve más desafiante con el tiempo
- **Streaks con Bonificación**: Multiplicadores de XP por días consecutivos completando tareas
- **Eventos Temporales**: Multiplicadores especiales por períodos limitados
- **Tareas Especiales**: Tareas únicas con fechas límite y XP personalizable
- **Estadísticas Detalladas**: Seguimiento completo del progreso del usuario

## 🎯 Sistema de Recompensas

### Experiencia por Dificultad
- **Fácil**: 10 XP (tareas cotidianas simples)
- **Media**: 25 XP (actividades que requieren más esfuerzo)
- **Difícil**: 50 XP (desafíos significativos)
- **Especial**: XP personalizable (eventos únicos)

### Multiplicadores
- **Streak Día 5**: 1.25x XP
- **Evento Actual**: 1.1x XP (10% extra)
- **Combinado**: 1.375x XP total

## 🛠️ Tecnologías

- **Backend**: Java 17, Spring Boot 3.2.0
- **Persistencia**: Spring Data JPA con herencia SINGLE_TABLE
- **Base de datos**: H2 (desarrollo/QA), PostgreSQL (producción)
- **DTOs**: Mappers para separación de capas y seguridad
- **Validación**: Bean Validation (@NotBlank, @Size, etc.)
- **Testing**: JUnit 5 + MockMvc (65 tests implementados)
- **Build**: Maven 3.6+

## 📦 Instalación y Uso

### 🚀 Inicio Rápido

```bash
# Clonar el repositorio
git clone [url-del-repo]
cd mi-proyecto

# Desarrollo (H2 en memoria)
./scripts/start-dev.sh

# QA con tests (H2 persistente)  
./scripts/start-qa.sh

# Producción (requiere PostgreSQL)
./scripts/start-prod.sh
```

### 🌍 Ambientes Disponibles

| Ambiente | Puerto | Base de Datos | Script | URL |
|----------|--------|---------------|--------|-----|
| **DEV** | 8080 | H2 memoria | `./scripts/start-dev.sh` | http://localhost:8080 |
| **QA** | 8081 | H2 archivo | `./scripts/start-qa.sh` | http://localhost:8081 |
| **PROD** | 8080 | PostgreSQL | `./scripts/start-prod.sh` | http://localhost:8080 |

### 🐳 Con Docker

```bash
# Desarrollo
docker-compose up --build

# Producción con PostgreSQL
export DB_PASSWORD=secure_password
docker-compose -f docker-compose.prod.yml up --build
```

## 🧪 Testing

### Ejecutar Pruebas
```bash
# Todos los tests (65 tests)
mvn test

# Solo tests unitarios (32 tests)
mvn test -Dtest="com.taskmanager.gamified.entity.*"

# Solo tests de integración (24 tests) 
mvn test -Dtest="com.taskmanager.gamified.integration.*"

# Solo tests de API/controlador (9 tests)
mvn test -Dtest="*TestControllerIntegrationTest"
```

### Cobertura
- **Tests Unitarios**: Entidades, validaciones, lógica de negocio
- **Tests de Integración**: Repositorios, queries custom, base de datos
- **Tests de Controlador**: API endpoints, DTOs, validación, manejo errores

### Arquitectura de Testing
- **Base de datos**: H2 en memoria (aislada por test)
- **Configuración**: `application-test.properties`
- **Datos**: `data.sql` carga automática
- **Frameworks**: JUnit 5, MockMvc, Spring Boot Test

## 📋 Ejemplos de Tareas

### Fáciles (10 XP)
- Beber un vaso de agua
- Dar un paseo de 5 minutos
- Hacer la cama
- Pasear al perro

### Medias (25 XP)
- Hacer ejercicio 30 minutos
- Cocinar una comida completa
- Meditar 15 minutos
- Estudiar 1 hora

### Difíciles (50 XP)
- Completar proyecto importante
- Ejercicio intenso 1 hora
- Limpiar toda la casa
- Aprender algo nuevo 2+ horas

## 🎮 Mecánicas de Juego

- **Nivel 1**: Requiere solo 25 XP (fácil de alcanzar)
- **Progresión**: Cada nivel siguiente requiere más XP
- **Streaks**: Bonus por completar tareas días consecutivos
- **Eventos**: Multiplicadores temporales especiales
- **Historial**: Seguimiento de los últimos 12 meses

## ✅ Estado del Proyecto

### Completado
- [x] **Entidades JPA**: Usuario, Tarea, TareaEspecial, Evento, HistorialTarea
- [x] **DTOs y Mappers**: Separación de capas, seguridad API
- [x] **Repositorios**: Queries optimizadas con CASE ORDER BY
- [x] **Base de datos**: H2 configurada con datos iniciales (21 tareas)
- [x] **Multi-ambiente**: DEV, QA, PROD con configuraciones específicas
- [x] **API REST**: Endpoints con DTOs y validación
- [x] **Testing**: 65 tests (unitarios, integración, controlador)
- [x] **Documentación**: CLAUDE.md, DTOS.md, DEPLOYMENT.md

### 🔮 Próximos Pasos
- [ ] Servicios de lógica de negocio (XP, niveles, streaks)
- [ ] Controladores REST completos (/api/users, /api/tasks)
- [ ] Sistema de logros y rankings
- [ ] Migración a OAuth2
- [ ] Interfaz web frontend (specs en FRONTEND_SPECS.md)
- [ ] Aplicación móvil

---

*Convierte tu productividad en un juego y alcanza tus objetivos de manera divertida* 🏆
