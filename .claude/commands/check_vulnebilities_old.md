# Comando: Validación y Reparación de Vulnerabilidades de Seguridad

## Información de Versión
- **Versión del comando**: v1.2.0
- **Última ejecución**: Nunca ejecutado
- **Próxima revisión recomendada**: Cada 30 días o antes de cada release

## Propósito
Instrucciones sistemáticas para detectar, analizar y reparar vulnerabilidades de seguridad en el proyecto Task Manager Gamificado siguiendo las mejores prácticas de OWASP y estándares de la industria.

## Herramientas de Detección

### = Herramientas Automáticas
1. **OWASP Dependency Check** - Vulnerabilidades en dependencias
2. **Maven Security Plugin** - Análisis de dependencias Java
3. **SpotBugs + FindSecBugs** - Análisis estático de código
4. **SonarQube Community** - Análisis de calidad y seguridad
5. **Snyk** - Vulnerabilidades en dependencias (opcional)

### =à Instalación de Herramientas
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

# Actualizar dependencias vulnerables
mvn versions:display-dependency-updates
mvn versions:use-latest-releases
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
**Revisar manualmente estos archivos:**
- `src/main/java/**/controller/*.java` - Validación de entrada
- `src/main/java/**/entity/*.java` - Exposición de datos sensibles
- `src/main/resources/application*.properties` - Configuraciones sensibles
- `pom.xml` - Dependencias y versiones

### FASE 3: Validaciones OWASP Top 10

#### 3.1 A01 - Broken Access Control
```bash
# Verificar endpoints sin autenticación
grep -r "@RestController\|@RequestMapping" src/ | grep -v "@PreAuthorize\|@Secured"

# Revisar controladores críticos
find src/ -name "*Controller.java" -exec grep -l "admin\|delete\|create" {} \;
```

#### 3.2 A02 - Cryptographic Failures
```bash
# Buscar hardcoded secrets
grep -r -i "password\|secret\|key\|token" src/ --include="*.java" --include="*.properties"

# Verificar uso de algoritmos débiles
grep -r "MD5\|SHA1\|DES" src/ --include="*.java"
```

#### 3.3 A03 - Injection
```bash
# Buscar posibles SQL injection
grep -r "createQuery\|createNativeQuery" src/ --include="*.java"

# Verificar validación de entrada
grep -r "@Valid\|@Validated" src/ --include="*.java"
```

#### 3.4 A04 - Insecure Design
```bash
# Revisar lógica de negocio crítica
find src/ -name "*Service.java" -exec grep -l "level\|xp\|streak" {} \;
```

#### 3.5 A05 - Security Misconfiguration
```bash
# Verificar configuraciones de producción
grep -r "debug\|trace\|show-sql" src/main/resources/

# Revisar configuración de actuator
grep -r "management.endpoints" src/main/resources/
```

#### 3.6 A06 - Vulnerable Components
```bash
# Listar dependencias con vulnerabilidades conocidas
mvn dependency:tree | grep -E "(SNAPSHOT|BETA|RC|M[0-9])"
```

### FASE 4: Pruebas Específicas de Seguridad

#### 4.1 Validación de DTOs
```bash
# Verificar que todos los DTOs tienen validaciones
find src/ -name "*DTO.java" -exec grep -L "@Valid\|@NotNull\|@Size" {} \;
```

#### 4.2 Verificación de Mappers
```bash
# Asegurar que mappers no expongan datos sensibles
find src/ -name "*Mapper.java" -exec grep -l "password\|token\|secret" {} \;
```

#### 4.3 Configuración de Base de Datos
```bash
# Verificar configuraciones de H2 y PostgreSQL
grep -r "h2.console\|show-sql\|ddl-auto" src/main/resources/
```

## Proceso de Reparación

### Reparación por Tipo de Vulnerabilidad

#### =' Dependencias Vulnerables
1. Identificar versión segura más reciente
2. Actualizar en `pom.xml`
3. Ejecutar tests completos
4. Verificar compatibilidad

#### =' Código Inseguro
1. Aplicar principio de menor privilegio
2. Implementar validación de entrada
3. Sanitizar datos de salida
4. Agregar logging de seguridad

#### =' Configuración Insegura
1. Remover configuraciones de debug en producción
2. Configurar HTTPS obligatorio
3. Limitar endpoints de actuator
4. Configurar CORS apropiadamente

### Plantillas de Reparación

#### Template: Actualización de Dependencia
```xml
<!-- ANTES -->
<dependency>
    <groupId>org.example</groupId>
    <artifactId>vulnerable-lib</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- DESPUÉS -->
<dependency>
    <groupId>org.example</groupId>
    <artifactId>vulnerable-lib</artifactId>
    <version>1.2.3</version> <!-- Versión segura -->
</dependency>
```

#### Template: Validación de DTO
```java
// ANTES
public class UsuarioDTO {
    private String nombre;
}

// DESPUÉS
public class UsuarioDTO {
    @NotBlank(message = "Nombre es obligatorio")
    @Size(min = 2, max = 50, message = "Nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$", message = "Nombre solo puede contener letras")
    private String nombre;
}
```

## Formato de Reporte de Vulnerabilidades

### Estructura del Reporte
```
ID: VULN-YYYY-MM-DD-XXX
Fecha: YYYY-MM-DD HH:MM:SS
Severidad: [CRÍTICA|ALTA|MEDIA|BAJA]
Categoría: [OWASP A01-A10|DEPENDENCY|CONFIG|CODE]
Estado: [DETECTADA|EN_PROGRESO|RESUELTA|ACEPTADA]

Descripción:
[Descripción detallada de la vulnerabilidad]

Ubicación:
- Archivo: src/main/java/com/ejemplo/Clase.java:123
- Dependencia: org.example:lib:1.0.0
- Configuración: application-prod.properties:45

Impacto:
[Descripción del impacto potencial]

CVE/CWE:
- CVE: CVE-2023-XXXXX (si aplica)
- CWE: CWE-XXX (si aplica)

Solución Aplicada:
[Descripción de la reparación implementada]

Verificación:
[Cómo se verificó que la vulnerabilidad fue resuelta]

Tiempo de Resolución: X horas/días
```

## Archivo de Historial

### Ubicación del Historial
- **Archivo principal**: `.security/vulnerability-history.md`
- **Reportes detallados**: `.security/reports/YYYY-MM-DD/`
- **Configuración**: `.security/security-config.json`

### Comando para Crear Estructura
```bash
mkdir -p .security/reports
touch .security/vulnerability-history.md
touch .security/security-config.json
```

## Automatización y Programación

### Script de Ejecución Completa
```bash
#!/bin/bash
# security-check.sh

echo "=== INICIANDO VALIDACIÓN DE SEGURIDAD ==="
DATE=$(date +%Y-%m-%d)
REPORT_DIR=".security/reports/$DATE"
mkdir -p "$REPORT_DIR"

# FASE 1: Dependencias
echo "1. Analizando dependencias..."
mvn org.sonatype.ossindex.maven:ossindex-maven-plugin:audit > "$REPORT_DIR/dependency-audit.txt"

# FASE 2: Código estático
echo "2. Análisis estático de código..."
mvn spotbugs:spotbugs
cp target/spotbugsXml.xml "$REPORT_DIR/"

# FASE 3: OWASP Top 10
echo "3. Validaciones OWASP..."
# Ejecutar validaciones específicas...

# FASE 4: Generar reporte
echo "4. Generando reporte consolidado..."
# Consolidar resultados...

echo "=== VALIDACIÓN COMPLETADA ==="
echo "Reportes generados en: $REPORT_DIR"
```

### Programación Automática
```bash
# Agregar a crontab para ejecución semanal
0 2 * * 1 /path/to/security-check.sh
```

## Lista de Verificación Final

###  Antes de Completar la Validación
- [ ] Todas las herramientas ejecutadas sin errores
- [ ] Vulnerabilidades críticas y altas resueltas
- [ ] Tests de seguridad pasando
- [ ] Documentación actualizada
- [ ] Historial de vulnerabilidades actualizado
- [ ] Configuraciones de producción validadas
- [ ] Dependencias actualizadas a versiones seguras

### =Ê Métricas a Reportar
- Número total de vulnerabilidades encontradas
- Vulnerabilidades por severidad (Crítica/Alta/Media/Baja)
- Tiempo promedio de resolución
- Vulnerabilidades recurrentes
- Porcentaje de cobertura de análisis

## Contactos de Escalamiento

### En caso de vulnerabilidades críticas:
1. **Inmediato**: Deshabilitar funcionalidad afectada
2. **1 hora**: Notificar a stakeholders
3. **4 horas**: Implementar fix temporal
4. **24 horas**: Implementar solución permanente

---

**Nota**: Este comando debe ejecutarse antes de cada release y mensualmente para mantener la seguridad del sistema.

## Próxima Actualización
- **Fecha programada**: 30 días desde última ejecución
- **Trigger**: Cambios en dependencias o código crítico
- **Responsable**: Equipo de desarrollo