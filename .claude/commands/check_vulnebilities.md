# Comando: Validación y Reparación de Vulnerabilidades de Seguridad

## Información de Versión
- **Versión del comando**: v1.3.0
- **Última ejecución**: Nunca ejecutado
- **Próxima revisión recomendada**: Cada 30 días o antes de cada release

## Propósito
Instrucciones sistemáticas para detectar, analizar y reparar vulnerabilidades de seguridad en el proyecto Task Manager Gamificado siguiendo las mejores prácticas de OWASP y estándares de la industria.

## ⚠️ PRINCIPIOS CRÍTICOS DE SEGURIDAD SIN RUPTURA

### 🔒 Regla de Oro: NO QUEBRAR FUNCIONALIDAD EXISTENTE
**ADVERTENCIA**: Este proyecto está diseñado para sistemas en producción. Cualquier reparación de vulnerabilidades DEBE mantener:

1. **Compatibilidad de API**: No cambiar nombres de parámetros, endpoints, o estructura de respuestas
2. **Comportamiento funcional**: La lógica de negocio debe permanecer idéntica
3. **Contratos de datos**: DTOs y entidades no deben cambiar su estructura pública
4. **Rendimiento**: Las correcciones no deben impactar significativamente el performance

### 🛡️ Estrategias de Reparación Seguras

#### ✅ PERMITIDO (Reparaciones no disruptivas):
- Agregar validaciones adicionales SIN cambiar parámetros existentes
- Implementar logging de seguridad sin afectar flujo principal
- Actualizar dependencias manteniendo compatibilidad de API
- Agregar headers de seguridad sin cambiar respuestas
- Configurar límites de rate limiting como feature nueva
- Implementar sanitización ANTES de procesar datos (no después)
- Agregar annotations de validación a campos existentes
- Configurar CORS sin cambiar endpoints
- Implementar filtros de seguridad transparentes

#### ❌ PROHIBIDO (Reparaciones disruptivas):
- Cambiar nombres de campos en DTOs o entidades
- Modificar estructura de endpoints REST existentes
- Alterar tipos de datos de parámetros públicos
- Cambiar comportamiento de métodos públicos existentes
- Remover campos aunque estén marcados como deprecated
- Modificar códigos de respuesta HTTP establecidos
- Cambiar formato de respuestas JSON
- Alterar parámetros de entrada requeridos/opcionales

### 🧪 VALIDACIÓN OBLIGATORIA DE REGRESIÓN

#### Antes de Aplicar Cualquier Fix:
1. **Ejecutar suite completa de tests**: `mvn test`
2. **Validar endpoints existentes**: Usar tests de integración
3. **Verificar DTOs**: Asegurar serialización/deserialización idéntica
4. **Probar flujos críticos**: XP, niveles, streaks, completar tareas
5. **Validar rendimiento**: No degradación > 10%

#### Proceso de Validación Post-Fix:
```bash
# 1. Tests unitarios y de integración
mvn test

# 2. Tests específicos de regresión
mvn test -Dtest="*IntegrationTest"

# 3. Validar contratos de API
# Ejecutar tests que validen estructura de respuestas JSON

# 4. Verificar funcionalidad core
# Tests de completar tareas, cálculo XP, sistema de niveles
```

## Herramientas de Detección

### 🔍 Herramientas Automáticas
1. **OWASP Dependency Check** - Vulnerabilidades en dependencias
2. **Maven Security Plugin** - Análisis de dependencias Java
3. **SpotBugs + FindSecBugs** - Análisis estático de código
4. **SonarQube Community** - Análisis de calidad y seguridad
5. **Snyk** - Vulnerabilidades en dependencias (opcional)

### 🛠️ Instalación de Herramientas
```bash
# OWASP Dependency Check
wget https://github.com/jeremylong/DependencyCheck/releases/download/v8.4.0/dependency-check-8.4.0-release.zip
unzip dependency-check-8.4.0-release.zip

# SpotBugs + FindSecBugs (vía Maven)
# Ya configurado en pom.xml si no existe

# SonarQube (Docker)
docker run -d --name sonarqube -p 9000:9000 sonarqube:community
```

## Proceso de Validación - PASO A PASO

### FASE 1: Análisis de Dependencias

#### 1.1 OWASP Dependency Check
```bash
# Ejecutar análisis completo
./dependency-check/bin/dependency-check.sh \
  --project "Task Manager Gamificado" \
  --scan . \
  --format ALL \
  --out ./security-reports/dependency-check

# Revisar reporte HTML generado
echo "Reporte generado en: ./security-reports/dependency-check/dependency-check-report.html"
```

#### 1.2 Maven Security Audit
```bash
# Verificar vulnerabilidades conocidas
mvn org.sonatype.ossindex.maven:ossindex-maven-plugin:audit

# IMPORTANTE: Antes de actualizar dependencias
echo "⚠️ VERIFICAR COMPATIBILIDAD ANTES DE ACTUALIZAR"
mvn versions:display-dependency-updates

# Solo actualizar si se confirma compatibilidad
# mvn versions:use-latest-releases
```

### FASE 2: Análisis Estático de Código

#### 2.1 SpotBugs + FindSecBugs
```bash
# Ejecutar análisis de seguridad
mvn compile spotbugs:spotbugs

# Generar reporte HTML
mvn spotbugs:gui
```

#### 2.2 Análisis Manual de Código Crítico
**Revisar manualmente estos archivos CON CUIDADO:**
- `src/main/java/**/controller/*.java` - Validación de entrada (NO cambiar parámetros)
- `src/main/java/**/entity/*.java` - Exposición de datos sensibles (NO cambiar campos)
- `src/main/java/**/dto/*.java` - DTOs públicos (NO modificar estructura)
- `src/main/resources/application*.properties` - Configuraciones sensibles
- `pom.xml` - Dependencias y versiones

### FASE 3: Validaciones OWASP Top 10

#### 3.1 A01 - Broken Access Control
```bash
# Verificar endpoints sin autenticación
grep -r "@RestController\|@RequestMapping" src/ | grep -v "@PreAuthorize\|@Secured"

# Revisar controladores críticos
find src/ -name "*Controller.java" -exec grep -l "admin\|delete\|create" {} \;

# ⚠️ REPARACIÓN SEGURA: Agregar @PreAuthorize sin cambiar endpoint
```

#### 3.2 A02 - Cryptographic Failures
```bash
# Buscar hardcoded secrets
grep -r -i "password\|secret\|key\|token" src/ --include="*.java" --include="*.properties"

# Verificar uso de algoritmos débiles
grep -r "MD5\|SHA1\|DES" src/ --include="*.java"

# ⚠️ REPARACIÓN SEGURA: Externalizar secrets, NO cambiar nombres de campos
```

#### 3.3 A03 - Injection
```bash
# Buscar posibles SQL injection
grep -r "createQuery\|createNativeQuery" src/ --include="*.java"

# Verificar validación de entrada
grep -r "@Valid\|@Validated" src/ --include="*.java"

# ⚠️ REPARACIÓN SEGURA: Agregar @Valid a DTOs existentes sin cambiar estructura
```

#### 3.4 A04 - Insecure Design
```bash
# Revisar lógica de negocio crítica
find src/ -name "*Service.java" -exec grep -l "level\|xp\|streak" {} \;

# ⚠️ REPARACIÓN SEGURA: Reforzar validaciones internas, NO cambiar API pública
```

#### 3.5 A05 - Security Misconfiguration
```bash
# Verificar configuraciones de producción
grep -r "debug\|trace\|show-sql" src/main/resources/

# Revisar configuración de actuator
grep -r "management.endpoints" src/main/resources/

# ⚠️ REPARACIÓN SEGURA: Configurar por ambiente, NO cambiar defaults existentes
```

#### 3.6 A06 - Vulnerable Components
```bash
# Listar dependencias con vulnerabilidades conocidas
mvn dependency:tree | grep -E "(SNAPSHOT|BETA|RC|M[0-9])"

# ⚠️ REPARACIÓN SEGURA: Actualizar solo versiones patch/minor compatibles
```

### FASE 4: Pruebas Específicas de Seguridad

#### 4.1 Validación de DTOs (CON PRECAUCIÓN)
```bash
# Verificar que todos los DTOs tienen validaciones
find src/ -name "*DTO.java" -exec grep -L "@Valid\|@NotNull\|@Size" {} \;

# ⚠️ REPARACIÓN SEGURA: Agregar validaciones SIN cambiar nombres de campos
```

#### 4.2 Verificación de Mappers (CRÍTICO)
```bash
# Asegurar que mappers no expongan datos sensibles
find src/ -name "*Mapper.java" -exec grep -l "password\|token\|secret" {} \;

# ⚠️ REPARACIÓN SEGURA: Filtrar datos internamente, NO cambiar métodos públicos
```

#### 4.3 Configuración de Base de Datos
```bash
# Verificar configuraciones de H2 y PostgreSQL
grep -r "h2.console\|show-sql\|ddl-auto" src/main/resources/

# ⚠️ REPARACIÓN SEGURA: Configurar por ambiente, mantener funcionalidad
```

## Proceso de Reparación SEGURA

### ⚡ PROCESO OBLIGATORIO ANTES DE CADA FIX

#### 1. Pre-Fix Validation
```bash
# Crear backup de la funcionalidad actual
git checkout -b security-fix-backup-$(date +%Y%m%d)

# Ejecutar todos los tests como baseline
mvn test > test-results-before.txt

# Documentar comportamiento actual
echo "Estado antes del fix:" > fix-documentation.md
curl -X GET http://localhost:8080/api/test/health >> fix-documentation.md
```

#### 2. Implementar Fix
```bash
# Aplicar corrección siguiendo principios no disruptivos
# Documentar cada cambio realizado
```

#### 3. Post-Fix Validation
```bash
# Ejecutar suite completa de tests
mvn test > test-results-after.txt

# Comparar resultados
diff test-results-before.txt test-results-after.txt

# Validar que no hay regresiones
if [ $? -ne 0 ]; then
    echo "⚠️ ALERTA: Se detectaron cambios en tests"
    echo "Revisar si son regresiones o mejoras esperadas"
fi

# Probar endpoints críticos
curl -X GET http://localhost:8080/api/test/health
curl -X GET http://localhost:8080/api/test/usuarios
curl -X GET http://localhost:8080/api/test/tareas
```

### Reparación por Tipo de Vulnerabilidad

#### 🔧 Dependencias Vulnerables (MÉTODO SEGURO)
```bash
# 1. Identificar versión segura más reciente COMPATIBLE
mvn versions:display-dependency-updates

# 2. Verificar notas de release para breaking changes
# 3. Actualizar solo versiones patch/minor
# 4. OBLIGATORIO: Ejecutar tests completos
mvn test

# 5. Si tests fallan, revertir y buscar alternativa
git checkout pom.xml  # si hay problemas
```

#### 🔧 Código Inseguro (MÉTODO SEGURO)
```java
// EJEMPLO: Agregar validación SIN cambiar API

// ANTES (vulnerable)
@PostMapping("/usuarios")
public UsuarioDTO crearUsuario(@RequestBody CrearUsuarioRequest request) {
    return usuarioService.crearUsuario(request);
}

// DESPUÉS (seguro, sin cambios de API)
@PostMapping("/usuarios")
public UsuarioDTO crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
    // Agregar validación adicional ANTES del procesamiento
    if (request.getNombre() != null) {
        request.setNombre(sanitizeInput(request.getNombre()));
    }
    return usuarioService.crearUsuario(request);
}

// MÉTODO HELPER INTERNO (no cambia API pública)
private String sanitizeInput(String input) {
    return input.trim().replaceAll("[<>\"']", "");
}
```

#### 🔧 Configuración Insegura (MÉTODO SEGURO)
```properties
# ANTES (inseguro)
spring.h2.console.enabled=true
spring.jpa.show-sql=true
logging.level.org.springframework.security=DEBUG

# DESPUÉS (seguro por ambiente)
# application-dev.properties (desarrollo)
spring.h2.console.enabled=true
spring.jpa.show-sql=true
logging.level.org.springframework.security=DEBUG

# application-prod.properties (producción)
spring.h2.console.enabled=false
spring.jpa.show-sql=false
logging.level.org.springframework.security=WARN

# MANTENER configuración default para compatibilidad
```

### Plantillas de Reparación SEGURAS

#### Template: Actualización de Dependencia SEGURA
```xml
<!-- PROCESO SEGURO -->
<!-- 1. VERIFICAR breaking changes en release notes -->
<!-- 2. Actualizar solo versiones compatibles -->
<!-- 3. EJECUTAR tests después de cada cambio -->

<!-- ANTES -->
<dependency>
    <groupId>org.example</groupId>
    <artifactId>vulnerable-lib</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- DESPUÉS (solo si es compatible) -->
<dependency>
    <groupId>org.example</groupId>
    <artifactId>vulnerable-lib</artifactId>
    <version>1.0.3</version> <!-- Versión patch segura -->
</dependency>
```

#### Template: Validación de DTO SEGURA
```java
// ANTES
public class UsuarioDTO {
    private String nombre;  // NO CAMBIAR NOMBRE
    private Integer nivel;  // NO CAMBIAR TIPO
}

// DESPUÉS (validación agregada SIN cambios)
public class UsuarioDTO {
    @NotBlank(message = "Nombre es obligatorio")
    @Size(min = 2, max = 50, message = "Nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Nombre solo puede contener letras")
    private String nombre;  // MISMO NOMBRE, MISMA VISIBILIDAD
    
    @Min(value = 1, message = "Nivel debe ser mayor a 0")
    @Max(value = 100, message = "Nivel no puede exceder 100")
    private Integer nivel;  // MISMO TIPO, MISMA VISIBILIDAD
    
    // TODOS LOS GETTERS/SETTERS DEBEN PERMANECER IDÉNTICOS
}
```

#### Template: Endpoint Seguro SIN Cambios
```java
// ANTES (vulnerable)
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/usuarios")
    public List<UsuarioDTO> getUsuarios() {
        return usuarioService.getAllUsuarios();
    }
}

// DESPUÉS (seguro, API idéntica)
@RestController
@RequestMapping("/api/test")
public class TestController {
    
    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('USER')")  // Seguridad agregada
    public List<UsuarioDTO> getUsuarios() {
        // Validación interna agregada
        logSecurityEvent("usuarios_accessed", getCurrentUser());
        
        // MISMA LÓGICA DE NEGOCIO
        return usuarioService.getAllUsuarios();
    }
    
    // Método helper interno (no afecta API)
    private void logSecurityEvent(String event, String user) {
        // Logging de seguridad
    }
}
```

## Formato de Reporte de Vulnerabilidades

### Estructura del Reporte ACTUALIZADA
```
ID: VULN-YYYY-MM-DD-XXX
Fecha: YYYY-MM-DD HH:MM:SS
Severidad: [CRÍTICA|ALTA|MEDIA|BAJA]
Categoría: [OWASP A01-A10|DEPENDENCY|CONFIG|CODE]
Estado: [DETECTADA|EN_PROGRESO|RESUELTA|ACEPTADA]
Impacto en Producción: [SÍ|NO]
Requiere Testing Regresión: [SÍ|NO]

Descripción:
[Descripción detallada de la vulnerabilidad]

Ubicación:
- Archivo: src/main/java/com/ejemplo/Clase.java:123
- Dependencia: org.example:lib:1.0.0
- Configuración: application-prod.properties:45

Impacto Potencial:
[Descripción del impacto potencial]

Riesgo de Ruptura:
[Evaluación del riesgo de quebrar funcionalidad existente]

CVE/CWE:
- CVE: CVE-2023-XXXXX (si aplica)
- CWE: CWE-XXX (si aplica)

Solución Propuesta:
[Descripción de la reparación que NO afecta API/funcionalidad]

Alternativas Consideradas:
[Otras opciones evaluadas y por qué se descartaron]

Validación Pre-Fix:
- [ ] Tests baseline ejecutados
- [ ] Comportamiento actual documentado
- [ ] Impacto en API evaluado

Solución Aplicada:
[Descripción exacta de lo implementado]

Validación Post-Fix:
- [ ] Todos los tests pasan
- [ ] No hay regresiones detectadas
- [ ] API mantiene compatibilidad
- [ ] Performance no degradado

Tiempo de Resolución: X horas/días
Validado Por: [Nombre/Claude]
```

## Lista de Verificación Final ACTUALIZADA

### ✅ Antes de Completar la Validación
- [ ] Todas las herramientas ejecutadas sin errores
- [ ] Vulnerabilidades críticas y altas resueltas SIN romper funcionalidad
- [ ] Tests de seguridad pasando
- [ ] **Tests de regresión pasando al 100%**
- [ ] **API endpoints mantienen compatibilidad total**
- [ ] **DTOs/entidades mantienen estructura pública**
- [ ] Documentación actualizada
- [ ] Historial de vulnerabilidades actualizado
- [ ] Configuraciones de producción validadas
- [ ] Dependencias actualizadas SOLO a versiones compatibles

### 🧪 Validación de Regresión Obligatoria
- [ ] `mvn test` ejecutado y pasando
- [ ] Tests de integración ejecutados
- [ ] Endpoints REST validados con misma respuesta
- [ ] Serialización/deserialización JSON idéntica
- [ ] Performance dentro de límites aceptables (+/- 10%)
- [ ] Logs de aplicación sin errores nuevos

### 📊 Métricas a Reportar
- Número total de vulnerabilidades encontradas
- Vulnerabilidades por severidad (Crítica/Alta/Media/Baja)
- **Vulnerabilidades reparadas sin impacto funcional**
- **Vulnerabilidades que requirieron cambios disruptivos (EVITAR)**
- Tiempo promedio de resolución
- Vulnerabilidades recurrentes
- Porcentaje de cobertura de análisis
- **Porcentaje de tests de regresión pasando**

## Escalamiento en Caso de Conflicto

### Si una Vulnerabilidad Requiere Cambios Disruptivos:

#### 🚨 Procedimiento de Escalamiento:
1. **PARAR** - No aplicar el fix inmediatamente
2. **DOCUMENTAR** - Registrar por qué la reparación requiere cambios disruptivos
3. **EVALUAR** - Considerar alternativas no disruptivas
4. **DECIDIR** - Evaluar si el riesgo de seguridad justifica el cambio disruptivo
5. **PLANIFICAR** - Si se procede, planificar migración gradual y comunicación

#### 🔄 Alternativas a Cambios Disruptivos:
- **Deprecation gradual**: Mantener API antigua, agregar nueva
- **Versionado de API**: Crear v2 sin afectar v1
- **Feature flags**: Habilitar nueva funcionalidad progresivamente
- **Configuración opcional**: Hacer el cambio configurable
- **Filtrado interno**: Cambiar lógica interna sin afectar contratos

---

## Contactos de Escalamiento ACTUALIZADO

### En caso de vulnerabilidades críticas:
1. **Inmediato**: Evaluar si se puede deshabilitar funcionalidad SIN afectar core
2. **1 hora**: Notificar a stakeholders sobre riesgo vs. estabilidad
3. **4 horas**: Implementar fix temporal que NO rompa funcionalidad
4. **24 horas**: Implementar solución permanente compatible
5. **Si no es posible fix compatible**: Escalar para aprobación de cambio disruptivo

---

**Nota**: Este comando debe ejecutarse antes de cada release y mensualmente para mantener la seguridad del sistema SIN comprometer la estabilidad de producción.

## Próxima Actualización
- **Fecha programada**: 30 días desde última ejecución
- **Trigger**: Cambios en dependencias o código crítico
- **Responsable**: Equipo de desarrollo
- **Enfoque**: Seguridad sin romper compatibilidad