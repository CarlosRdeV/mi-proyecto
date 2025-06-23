# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Configuración de Idioma

**IMPORTANTE**: Todas las interacciones deben realizarse en español por defecto.

## Descripción del Proyecto

**Task Manager Gamificado** - API RESTful desarrollada con Java Spring Boot que convierte la gestión de tareas en una experiencia de videojuego, donde los usuarios ganan experiencia, suben de nivel y mantienen streaks por completar tareas.

## Tecnologías Utilizadas

- **Backend**: Java 17, Spring Boot 3.2.0
- **Base de Datos**: H2 (desarrollo/QA), PostgreSQL (producción)
- **Persistencia**: Spring Data JPA con herencia SINGLE_TABLE
- **DTOs**: Mappers para separación de capas y seguridad
- **Validación**: Bean Validation con annotations
- **Build Tool**: Maven
- **Testing**: JUnit 5 + Mockito + Spring Boot Test
- **Containerización**: Docker, Docker Compose
- **Monitoreo**: Spring Boot Actuator
- **Autenticación**: Simple ID (preparado para OAuth2 futuro)

## Ambientes Configurados

### 🔧 DEV (Desarrollo)
- **Puerto**: 8080
- **Base de datos**: H2 en memoria
- **Configuración**: `application-dev.properties`
- **Características**: Logging completo, H2 Console, Hot reload

### 🧪 QA (Quality Assurance)  
- **Puerto**: 8081
- **Base de datos**: H2 persistente en archivo
- **Configuración**: `application-qa.properties`
- **Características**: Tests automáticos, datos de prueba, logging moderado

### 🏭 PROD (Producción)
- **Puerto**: 8080
- **Base de datos**: PostgreSQL
- **Configuración**: `application-prod.properties`
- **Características**: Seguridad maximizada, logging mínimo, SSL opcional

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
- **Usuario**: ID, nombre, nivel, XP, streak actual, fecha último login, historial tareas
- **Tarea**: ID, nombre, descripción, dificultad, XP base, repetible, activa (clase base)
- **TareaEspecial**: Hereda de Tarea, incluye fecha límite, una_vez_por_usuario
- **HistorialTarea**: Usuario, tarea, fecha completación, XP ganado, multiplicadores
- **Evento**: ID, nombre, multiplicador, fecha inicio, fecha fin, activo
- **DificultadTarea**: Enum con XP base (FACIL=10, MEDIA=25, DIFICIL=50, ESPECIAL=0)

### DTOs Implementados
- **UsuarioDTO**: Perfil de usuario sin relaciones complejas
- **TareaDTO**: Información de tarea con tipo discriminador
- **EventoDTO**: Datos de evento con fechas de vigencia
- **EstadisticasDTO**: Resumen de contadores con timestamp
- **CrearUsuarioRequest**: DTO de entrada con validaciones

### Reglas de Negocio
- Historial se mantiene solo por 12 meses
- Tareas repetibles: máximo 1 por día por usuario
- Streaks se pierden si no se completa al menos 1 tarea por día
- XP final = XP base × multiplicador streak × multiplicador evento

## Arquitectura del Proyecto

### Estructura de Capas
```
src/main/java/com/taskmanager/gamified/
├── entity/           # Entidades JPA
│   ├── Usuario.java       # Usuario con XP, nivel, streak
│   ├── Tarea.java         # Clase base para tareas
│   ├── TareaEspecial.java # Tareas con fecha límite
│   ├── HistorialTarea.java # Historial de completaciones
│   ├── Evento.java        # Eventos multiplicadores
│   └── DificultadTarea.java # Enum de dificultades
├── dto/              # Data Transfer Objects
│   ├── UsuarioDTO.java         # Usuario sin relaciones lazy
│   ├── TareaDTO.java           # Tarea con discriminador tipo
│   ├── EventoDTO.java          # Evento con fechas vigencia
│   ├── EstadisticasDTO.java    # Contadores del sistema
│   └── CrearUsuarioRequest.java # Request con validaciones
├── mapper/           # Convertidores Entity ↔ DTO
│   ├── UsuarioMapper.java      # Usuario entity ↔ DTO
│   ├── TareaMapper.java        # Tarea + herencia ↔ DTO
│   └── EventoMapper.java       # Evento entity ↔ DTO
├── repository/       # Interfaces JPA Repository
│   ├── UsuarioRepository.java     # CRUD + queries custom
│   ├── TareaRepository.java       # CRUD + orden por dificultad
│   ├── EventoRepository.java      # CRUD + eventos vigentes
│   └── HistorialRepository.java   # CRUD + estadísticas
└── controller/       # Controladores REST
    └── TestController.java        # API endpoints con DTOs
```

### Principios de Diseño
- **Separación de capas**: Entity → DTO via Mappers
- **Herencia JPA**: SINGLE_TABLE para Tarea/TareaEspecial
- **DTOs para seguridad**: No exposición de relaciones lazy
- **Validación en DTOs**: Bean Validation en requests
- **Queries optimizadas**: ORDER BY con CASE para enums
- **Tests por capa**: Unitarios, integración, controlador

## Comandos de Desarrollo

### Comandos Básicos
```bash
# Compilar proyecto
mvn clean compile

# Ejecutar tests (todos)
mvn test

# Ejecutar solo tests unitarios
mvn test -Dtest="com.taskmanager.gamified.entity.*"

# Ejecutar solo tests de integración  
mvn test -Dtest="com.taskmanager.gamified.integration.*"

# Ejecutar solo tests de controlador
mvn test -Dtest="*TestControllerIntegrationTest"

# Generar JAR
mvn clean package

# Ejecutar aplicación directamente
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Por Ambiente
```bash
# Desarrollo (Puerto 8080)
./scripts/start-dev.sh

# QA (Puerto 8081)
./scripts/start-qa.sh

# Producción (Puerto 8080)
./scripts/start-prod.sh
```

### Docker
```bash
# Desarrollo
docker-compose up --build

# Producción con PostgreSQL
docker-compose -f docker-compose.prod.yml up --build
```

## Endpoints API

### Endpoints Actuales (Test Controller)
```bash
# Health Check
GET /api/test/health
# Response: { "status": "OK", "message": "...", "timestamp": "..." }

# Obtener todos los usuarios (DTO)
GET /api/test/usuarios
# Response: [UsuarioDTO...]

# Obtener todas las tareas activas (DTO, ordenadas por dificultad)
GET /api/test/tareas  
# Response: [TareaDTO...]

# Obtener eventos vigentes (DTO)
GET /api/test/eventos
# Response: [EventoDTO...]

# Obtener estadísticas del sistema (DTO)
GET /api/test/stats
# Response: EstadisticasDTO

# Crear usuario (DTO + validación)
POST /api/test/usuarios
# Body: CrearUsuarioRequest
# Response: UsuarioDTO | ErrorResponse
```

### Endpoints Futuros (En desarrollo)
```bash
# Usuarios
GET /api/users/{id} - Obtener perfil de usuario
POST /api/users - Crear usuario  
GET /api/users/{id}/stats - Estadísticas del usuario

# Tareas
GET /api/tasks - Obtener todas las tareas disponibles
POST /api/tasks/{id}/complete - Completar tarea
GET /api/users/{userId}/history - Historial de tareas completadas

# Eventos  
GET /api/events/active - Eventos activos
POST /api/events - Crear evento (admin)
```

## Testing

### Estructura de Pruebas
```
src/test/java/
├── com/taskmanager/gamified/
│   ├── entity/                 # Tests unitarios de entidades
│   │   ├── UsuarioTest.java    # 4 tests - validaciones y lógica
│   │   ├── TareaTest.java      # 6 tests - herencia y validaciones  
│   │   ├── TareaEspecialTest.java # 6 tests - funciones específicas
│   │   ├── EventoTest.java     # 7 tests - vigencia y lógica
│   │   ├── HistorialTareaTest.java # 6 tests - cálculos XP
│   │   └── DificultadTareaTest.java # 3 tests - enum XP
│   ├── integration/           # Tests de integración  
│   │   ├── UsuarioRepositoryIntegrationTest.java    # Repository + DB
│   │   ├── TareaRepositoryIntegrationTest.java     # Query custom + orden
│   │   ├── EventoRepositoryIntegrationTest.java    # Consultas vigencia
│   │   └── HistorialRepositoryIntegrationTest.java # Agregaciones
│   └── controller/            # Tests de controlador web
│       └── TestControllerIntegrationTest.java # 9 tests - API endpoints
```

### Cobertura de Tests
- **Tests Unitarios**: 32 tests (entidades y lógica de negocio)
- **Tests de Integración**: 24 tests (repositorios y base de datos)  
- **Tests de Controlador**: 9 tests (API endpoints y DTOs)
- **Total**: 65 tests cubriendo funcionalidad crítica

### Configuración de Testing
- **Perfil test**: `application-test.properties`
- **Base de datos**: H2 en memoria (aislada por test)
- **Datos iniciales**: `data.sql` cargado automáticamente
- **Validaciones**: Bean Validation activa en tests
- **MockMvc**: Para tests de controlador web

### Ejecutar Tests
```bash
# Todos los tests
mvn test

# Solo tests unitarios (entidades)
mvn test -Dtest="com.taskmanager.gamified.entity.*"

# Solo tests de integración (repositorios) 
mvn test -Dtest="com.taskmanager.gamified.integration.*"

# Solo tests de controlador (API)
mvn test -Dtest="*TestControllerIntegrationTest"

# Con perfil específico
mvn test -Dspring.profiles.active=test
```

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

## Estado Actual del Desarrollo

### ✅ Completado
- **Arquitectura base**: Entidades JPA con herencia, DTOs, Mappers
- **Base de datos**: H2 configurada con 21 tareas iniciales  
- **Multi-ambiente**: DEV, QA, PROD completamente configurados
- **API REST**: Endpoints con DTOs y validación Bean Validation
- **Testing completo**: 65 tests (unitarios, integración, controlador)
- **Documentación**: CLAUDE.md, DTOS.md, DEPLOYMENT.md actualizados

### 🔄 En Desarrollo
- Servicios de lógica de negocio (XP, niveles, streaks)
- Controladores REST completos (/api/users, /api/tasks, /api/events)
- Sistema de logros y estadísticas avanzadas

### 📋 Próximos Pasos
1. Implementar servicios de negocio para cálculo de XP y niveles
2. Crear controladores REST para funcionalidad completa
3. Sistema de logros y rankings entre usuarios
4. Migración a OAuth2 para autenticación
5. Frontend web (especificaciones en FRONTEND_SPECS.md)

## Escalabilidad Futura

El proyecto está diseñado para empezar como sistema individual pero puede escalarse para múltiples usuarios:
- **Autenticación**: Preparado para migración a OAuth2
- **Base de datos**: Migración a PostgreSQL/MySQL ya configurada
- **Arquitectura**: DTOs y mappers facilitan evolución de API
- **Testing**: Cobertura amplia facilita refactoring seguro
- **Frontend**: Especificaciones completas para desarrollo paralelo
- **Funcionalidades**: Rankings globales, logros, PWA capabilities