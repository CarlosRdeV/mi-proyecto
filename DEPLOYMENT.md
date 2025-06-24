# Guía de Despliegue - Task Manager Gamificado
**Versión v1.3.0** - *Sistema completo con API REST funcional*

## 🚀 Ambientes Configurados

El proyecto está configurado para ejecutarse en **3 ambientes** diferentes:

### 🔧 DEV (Desarrollo)
- **Puerto**: 8080
- **Base de datos**: H2 en memoria
- **Logging**: DEBUG completo
- **Consola H2**: Habilitada
- **Hot reload**: Disponible

### 🧪 QA (Quality Assurance)
- **Puerto**: 8081  
- **Base de datos**: H2 persistente en archivo
- **Logging**: INFO/WARN
- **Tests**: Se ejecutan antes de iniciar
- **Datos de prueba**: Incluidos

### 🏭 PROD (Producción)
- **Puerto**: 8080
- **Base de datos**: PostgreSQL
- **Logging**: Solo WARN/ERROR
- **Seguridad**: Maximizada
- **SSL**: Configurable

## 📋 Requisitos Previos

### Para todos los ambientes:
- Java 17 o superior
- Maven 3.6 o superior

### Para producción adicional:
- PostgreSQL 12 o superior
- Certificados SSL (opcional)

## 🚀 Inicio Rápido

### Desarrollo
```bash
# Usando script
./scripts/start-dev.sh

# O manualmente
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

### QA
```bash
# Usando script (incluye tests)
./scripts/start-qa.sh

# O manualmente
export SPRING_PROFILES_ACTIVE=qa
mvn clean test && mvn spring-boot:run
```

### Producción
```bash
# 1. Configurar variables de entorno
export DATABASE_URL=jdbc:postgresql://localhost:5432/taskmanager_prod
export DATABASE_USERNAME=taskmanager_user
export DATABASE_PASSWORD=secure_password

# 2. Ejecutar script
./scripts/start-prod.sh

# O manualmente
export SPRING_PROFILES_ACTIVE=prod
mvn clean package -DskipTests
java -jar target/gamified-taskmanager-1.0.0.jar
```

## 🐳 Despliegue con Docker

### Desarrollo
```bash
docker-compose up --build
```

### Producción con PostgreSQL
```bash
# 1. Configurar contraseña
export DB_PASSWORD=secure_password

# 2. Ejecutar
docker-compose -f docker-compose.prod.yml up --build
```

## 🔧 Configuración por Ambiente

### Variables de Entorno

| Variable | DEV | QA | PROD | Descripción |
|----------|-----|----|----- |-------------|
| `SPRING_PROFILES_ACTIVE` | `dev` | `qa` | `prod` | Ambiente activo |
| `DATABASE_URL` | - | - | ✅ | URL de PostgreSQL |
| `DATABASE_USERNAME` | - | - | ✅ | Usuario de BD |
| `DATABASE_PASSWORD` | - | - | ✅ | Contraseña de BD |
| `CORS_ALLOWED_ORIGINS` | `*` | Limited | ✅ | Dominios permitidos |
| `SSL_ENABLED` | `false` | `false` | Opcional | Habilitar HTTPS |

### Puertos y URLs

| Ambiente | Puerto | H2 Console | Actuator | Base de Datos |
|----------|--------|------------|----------|---------------|
| **DEV** | 8080 | ✅ `/h2-console` | ✅ `/actuator/*` | H2 memoria |
| **QA** | 8081 | ✅ `/h2-console` | Limited | H2 archivo |
| **PROD** | 8080 | ❌ | ⚠️ `/actuator/health` | PostgreSQL |

## 🧪 Ejecutar Tests

```bash
# Tests completos
./scripts/run-tests.sh

# Solo unitarios
mvn test -Dtest="**/*Test"

# Solo integración
mvn test -Dtest="**/*IntegrationTest"
```

## 📊 Monitoreo y Health Checks

### Health Check Endpoints
```bash
# Desarrollo
curl http://localhost:8080/actuator/health

# QA  
curl http://localhost:8081/actuator/health

# Producción
curl http://localhost:8080/actuator/health
```

### Logs por Ambiente

| Ambiente | Ubicación | Nivel | Rotación |
|----------|-----------|-------|----------|
| **DEV** | Console | DEBUG | - |
| **QA** | Console | INFO | - |
| **PROD** | `logs/taskmanager.log` | WARN | 100MB/30días |

## 🔒 Configuración de Seguridad

### Desarrollo
- CORS: Permisivo
- H2 Console: Habilitada
- Actuator: Todos los endpoints

### QA
- CORS: Limitado a hosts específicos
- H2 Console: Con autenticación
- Actuator: Endpoints limitados

### Producción
- CORS: Solo dominios autorizados
- H2 Console: Deshabilitada
- Actuator: Solo health y info
- SSL: Configurable
- Logs: Sin stack traces

## 🗄️ Base de Datos

### Conexión H2 (DEV/QA)
```
URL: jdbc:h2:mem:taskmanager_dev (DEV)
URL: jdbc:h2:file:./data/taskmanager_qa (QA)
Usuario: sa
Contraseña: (vacía en DEV, 'qa_password' en QA)
```

### PostgreSQL (PROD)
```sql
-- Crear base de datos
CREATE DATABASE taskmanager_prod;
CREATE USER taskmanager_user WITH PASSWORD 'secure_password';
GRANT ALL PRIVILEGES ON DATABASE taskmanager_prod TO taskmanager_user;
```

## 🔄 CI/CD Pipeline Sugerido

```yaml
stages:
  - test:
      run: ./scripts/run-tests.sh
  - build-qa:
      run: mvn clean package -Pqa
  - deploy-qa:
      run: ./scripts/start-qa.sh
  - integration-tests:
      run: curl http://qa-server:8081/actuator/health
  - build-prod:
      run: mvn clean package -Pprod -DskipTests
  - deploy-prod:
      run: ./scripts/start-prod.sh
```

## 🚨 Troubleshooting

### Problemas Comunes

1. **Puerto ocupado**
   ```bash
   # Verificar puertos
   lsof -i :8080
   lsof -i :8081
   ```

2. **Error de conexión a BD en PROD**
   ```bash
   # Verificar variables
   echo $DATABASE_URL
   echo $DATABASE_USERNAME
   
   # Test de conexión
   psql $DATABASE_URL -c "SELECT version();"
   ```

3. **Tests fallan en QA**
   ```bash
   # Ejecutar tests específicos
   mvn test -Dtest="UsuarioRepositoryIntegrationTest"
   ```

4. **Memoria insuficiente**
   ```bash
   # Ajustar JAVA_OPTS según ambiente
   export JAVA_OPTS="-Xms1024m -Xmx2048m"
   ```

## 📞 Soporte

Para problemas específicos del ambiente:
- **DEV**: Verificar logs en console
- **QA**: Revisar `./data/` y logs
- **PROD**: Revisar `./logs/taskmanager.log`

---

**Nota**: Mantener este archivo actualizado cuando se agreguen nuevas configuraciones o ambientes.