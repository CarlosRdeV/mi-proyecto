# Task Manager Gamificado 🎮📋

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

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Base de datos H2 (desarrollo)
- Maven

## 📦 Instalación y Uso

```bash
# Clonar el repositorio
git clone [url-del-repo]

# Navegar al directorio
cd mi-proyecto

# Compilar
mvn clean compile

# Ejecutar
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

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

## 🔮 Roadmap

- [ ] Implementación básica del sistema
- [ ] Sistema de logros
- [ ] Rankings entre usuarios
- [ ] Migración a OAuth2
- [ ] Base de datos PostgreSQL/MySQL
- [ ] Interfaz web frontend
- [ ] Aplicación móvil

---

*Convierte tu productividad en un juego y alcanza tus objetivos de manera divertida* 🏆
