# Frontend Specifications - Task Manager Gamificado
**Versión v1.3.0** - *Sincronizado con backend funcional*

## Información del Proyecto
- **Versión Backend**: v1.3.0 (API REST completa funcional)
- **Versión Frontend**: v1.3.0 (pendiente implementación)
- **Sincronización**: 2024-12-23
- **Estado**: Especificaciones actualizadas - Backend listo para frontend

## Stack Tecnológico Propuesto

### Core Framework
- **React 18.2+** con TypeScript 5+
- **Vite 5+** como build tool
- **Node.js 18+** para desarrollo

### Estilos y UI
- **Tailwind CSS 3.4+** - Utility-first CSS con tema retro personalizado
- **Headless UI** - Componentes accesibles sin estilos
- **Pixelated Icons** - Iconografía pixel art estilo 8-bit
- **Framer Motion 11+** - Animaciones y transiciones retro
- **Google Fonts** - Fuentes pixel perfect (Press Start 2P, VT323)

### Gestión de Estado
- **TanStack Query v5** - Server state management
- **Zustand 4+** - Estado global ligero
- **React Hook Form** - Manejo de formularios

### Networking y Seguridad
- **Axios 1.6+** - Cliente HTTP con interceptors
- **Zod 3.22+** - Validación de schemas
- **js-cookie 3.0+** - Manejo seguro de cookies
- **Auth0** o **Firebase Auth** - Autenticación (futuro OAuth2)

### Desarrollo y Testing
- **ESLint 8+** - Linting
- **Prettier 3+** - Formateo de código
- **Vitest** - Testing framework
- **Testing Library** - Testing utilities

### Librerías Específicas Retro
- **canvas-confetti** - Efectos de confetti pixelado
- **howler.js** - Manejo de audio 8-bit
- **pixi.js** (opcional) - Gráficos 2D avanzados para animaciones
- **react-spring** - Animaciones fluidas con easing retro
- **lottie-react** - Animaciones vectoriales para iconos pixelados

## Arquitectura del Frontend

### Estructura de Carpetas
```
src/
├── components/          # Componentes reutilizables
│   ├── ui/             # Componentes base (Button, Input, etc.)
│   ├── game/           # Componentes específicos de gamificación
│   └── layout/         # Componentes de layout
├── pages/              # Páginas principales
├── hooks/              # Custom hooks
├── services/           # Servicios API
├── stores/             # Estado global (Zustand)
├── types/              # Definiciones TypeScript
├── utils/              # Utilidades
└── constants/          # Constantes
```

### Páginas Principales
1. **Dashboard** - Vista principal con estadísticas del usuario
2. **Tasks** - Lista de tareas disponibles y completadas
3. **Profile** - Perfil del usuario y progreso
4. **Leaderboard** - Rankings (futuro)
5. **Events** - Eventos activos y históricos

## Componentes de Gamificación

### Componentes UI Especializados (Estilo Retro)
- **XPBar** - Barra de progreso pixelada estilo RPG con efectos de brillo
- **LevelBadge** - Medallón pixelado con animación de destello
- **StreakCounter** - Contador digital estilo arcade con números pixelados
- **TaskCard** - Tarjetas con bordes pixelados y efectos hover neón
- **RewardAnimation** - Explosión de partículas pixeladas con "+XP" flotante
- **ProgressRing** - Anillo circular pixelado con efectos de carga
- **RetroButton** - Botones 3D pixelados con efectos de presión
- **PixelAvatar** - Avatar del usuario en estilo pixel art
- **NeonGlow** - Componente wrapper para efectos de brillo neón

### Sistema de Notificaciones Retro
- **Toast notifications pixeladas** con bordes dentados y colores neón
- **Level up animation** - Explosión de píxeles con mensaje "LEVEL UP!"
- **Achievement unlocked** - Popup estilo consola retro
- **Sound effects 8-bit** - Biblioteca de sonidos clásicos arcade
- **Screen shake** - Efecto de vibración en pantalla para eventos importantes

## API Integration

### Endpoints Backend (Sincronizado con CLAUDE.md)

#### Usuarios
```typescript
GET /api/users/{id}           // Perfil usuario
POST /api/users               // Crear usuario  
GET /api/users/{id}/stats     // Estadísticas
```

#### Tareas
```typescript
GET /api/tasks                // Todas las tareas
POST /api/tasks/{id}/complete // Completar tarea
GET /api/users/{userId}/history // Historial
```

#### Eventos
```typescript
GET /api/events/active        // Eventos activos
POST /api/events              // Crear evento (admin)
```

### Tipos TypeScript (Sincronizados con Backend)

```typescript
// Usuario
interface User {
  id: string;
  nombre: string;
  nivel: number;
  experiencia: number;
  streakActual: number;
  fechaUltimoLogin: Date;
}

// Tarea
interface Task {
  id: string;
  nombre: string;
  descripcion: string;
  dificultad: 'FACIL' | 'MEDIA' | 'DIFICIL' | 'ESPECIAL';
  xpBase: number;
  repetible: boolean;
  activa: boolean;
}

// Evento
interface Event {
  id: string;
  nombre: string;
  multiplicador: number;
  fechaInicio: Date;
  fechaFin: Date;
  activo: boolean;
}

// Historial
interface TaskHistory {
  id: string;
  usuarioId: string;
  tareaId: string;
  fechaCompletacion: Date;
  xpGanado: number;
}
```

## Seguridad Frontend

### Validación de Datos
- **Input sanitization** con Zod schemas
- **XSS protection** - Escape de contenido dinámico
- **CSRF protection** - Tokens CSRF en formularios

### Autenticación
- **JWT tokens** almacenados en httpOnly cookies
- **Refresh token rotation**
- **Session timeout** automático
- **Login state persistence** segura

### Comunicación API
- **HTTPS only** en producción
- **Request/Response interceptors** para auth
- **Error handling** centralizado
- **Rate limiting** en cliente

## Experiencia de Usuario (UX)

### Tema Visual Retro Gaming
- **Estética 8-bit/16-bit** inspirada en videojuegos clásicos
- **Paleta de colores retro** - Verdes neón, azules eléctricos, magentas
- **Tipografía pixel perfect** - Fuentes pixeladas (Press Start 2P, VT323)
- **UI Elements pixelados** - Botones, barras, marcos con bordes pixelados
- **Efectos CRT** - Líneas de escaneo sutiles, glow effects
- **Sprites y iconos 8-bit** - Iconografía estilo arcade

### Gamificación Visual
- **Colores progresivos** por nivel estilo RPG clásico:
  - Nivel 1-10: Verde neón (#00FF41)
  - Nivel 11-25: Azul eléctrico (#1E90FF) 
  - Nivel 26-50: Magenta (#FF1493)
  - Nivel 51+: Dorado pixelado (#FFD700)
- **Animaciones retro** - Efectos de transición tipo arcade
- **Sound effects 8-bit** - Beeps y boops para feedback
- **Particle effects** - Explosiones pixeladas al completar tareas
- **Health bars style** - Barras de progreso estilo videojuego

### Responsividad
- **Mobile-first design**
- **PWA capabilities** (futuro)
- **Touch-friendly** interfaces
- **Offline support** básico

### Accesibilidad
- **ARIA labels** completos
- **Keyboard navigation**
- **Color contrast** WCAG AA
- **Screen reader** compatible

## Estados de Desarrollo

### Fase 1 - MVP (v1.0.0)
- [ ] Setup inicial del proyecto
- [ ] Componentes básicos de UI
- [ ] Integración con API backend
- [ ] Dashboard principal
- [ ] Sistema de tareas básico

### Fase 2 - Gamificación (v1.1.0)
- [ ] Animaciones de XP y nivel
- [ ] Sistema de streaks visual
- [ ] Eventos multiplicadores
- [ ] Estadísticas avanzadas

### Fase 3 - Social (v1.2.0)
- [ ] Rankings y leaderboards
- [ ] Sistema de logros
- [ ] Compartir progreso

## Configuración Visual Retro

### Paleta de Colores
```css
/* Colores principales */
--neon-green: #00FF41;      /* Matrix green */
--electric-blue: #1E90FF;   /* Arcade blue */
--hot-magenta: #FF1493;     /* Cyber pink */
--pixel-gold: #FFD700;      /* Achievement gold */
--dark-bg: #0a0a0a;         /* Background negro */
--gray-panel: #1a1a1a;      /* Paneles oscuros */

/* Colores de acento */
--neon-cyan: #00FFFF;       /* Highlights */
--warning-red: #FF073A;     /* Errores/peligro */
--success-green: #39FF14;   /* Éxito */
```

### Fuentes Retro
```css
/* Fuente principal - pixelada */
@import url('https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap');

/* Fuente secundaria - más legible */
@import url('https://fonts.googleapis.com/css2?family=VT323:wght@400&display=swap');

/* Fuente para números grandes */
@import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;700;900&display=swap');
```

### Efectos CSS Personalizados
```css
/* Glow effect para elementos neón */
.neon-glow {
  text-shadow: 0 0 10px currentColor;
  box-shadow: 0 0 20px currentColor;
}

/* Bordes pixelados */
.pixel-border {
  border-image: url('data:image/svg+xml,<svg>...</svg>') 2;
}

/* Efecto CRT */
.crt-effect {
  background: linear-gradient(transparent 50%, rgba(0,255,0,0.03) 50%);
  background-size: 100% 4px;
}
```

### Recursos de Audio 8-bit
- **Completar tarea**: Sonido "ding" clásico
- **Subir de nivel**: Fanfare corta estilo Mario
- **Streak bonus**: Sonido de power-up
- **Error**: Sonido de "game over" suave
- **Background**: Música ambiente opcional (activable/desactivable)

## Configuración de Desarrollo

### Variables de Entorno
```env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_APP_VERSION=1.0.0
VITE_AUTH_DOMAIN=tu-auth-domain
VITE_SENTRY_DSN=tu-sentry-dsn
```

### Scripts NPM
```json
{
  "dev": "vite",
  "build": "tsc && vite build",
  "preview": "vite preview",
  "test": "vitest",
  "lint": "eslint . --ext ts,tsx",
  "format": "prettier --write ."
}
```

## Performance y Optimización

### Estrategias
- **Code splitting** por rutas
- **Lazy loading** de componentes pesados
- **Memoization** de cálculos costosos
- **Virtual scrolling** para listas largas
- **Image optimization** automática

### Métricas a Monitorear
- **First Contentful Paint (FCP)**
- **Largest Contentful Paint (LCP)**
- **Time to Interactive (TTI)**
- **Bundle size** < 500KB inicial

## Próximos Pasos

1. **Setup del proyecto** con Vite + React + TypeScript
2. **Configuración de herramientas** (ESLint, Prettier, etc.)
3. **Implementación de componentes base**
4. **Integración con API backend**
5. **Testing y optimización**

---

**Nota**: Este archivo debe actualizarse cada vez que se modifique el backend para mantener sincronización entre frontend y backend.

**Última actualización**: 2024-06-23 | **Revisar antes de**: 2024-07-23