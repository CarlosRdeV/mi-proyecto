# Comando: Validaci�n y Reparaci�n de Vulnerabilidades de Seguridad

## Informaci�n de Versi�n
- **Versi�n del comando**: v1.2.0
- **�ltima ejecuci�n**: Nunca ejecutado
- **Pr�xima revisi�n recomendada**: Cada 30 d�as o antes de cada release

## Prop�sito
Instrucciones sistem�ticas para detectar, analizar y reparar vulnerabilidades de seguridad en el proyecto Task Manager Gamificado siguiendo las mejores pr�cticas de OWASP y est�ndares de la industria.

## Herramientas de Detecci�n

### = Herramientas Autom�ticas
1. **OWASP Dependency Check** - Vulnerabilidades en dependencias
2. **Maven Security Plugin** - An�lisis de dependencias Java
3. **SpotBugs + FindSecBugs** - An�lisis est�tico de c�digo
4. **SonarQube Community** - An�lisis de calidad y seguridad
5. **Snyk** - Vulnerabilidades en dependencias (opcional)

### =� Instalaci�n de Herramientas
```bash
# OWASP Dependency Check
wget https://github.com/jeremylong/DependencyCheck/releases/download/v8.4.0/dependency-check-8.4.0-release.zip
unzip dependency-check-8.4.0-release.zip

# SpotBugs + FindSecBugs (v�a Maven)
# Ya configurado en pom.xml si no existe

# SonarQube (Docker)
docker run -d --name sonarqube -p 9000:9000 sonarqube:community
```

## Proceso de Validaci�n - PASO A PASO

### FASE 1: An�lisis de Dependencias

#### 1.1 OWASP Dependency Check
```bash
# Ejecutar an�lisis completo
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

### FASE 2: An�lisis Est�tico de C�digo

#### 2.1 SpotBugs + FindSecBugs
```bash
# Ejecutar an�lisis de seguridad
mvn compile spotbugs:spotbugs

# Generar reporte HTML
mvn spotbugs:gui
```

#### 2.2 An�lisis Manual de C�digo Cr�tico
**Revisar manualmente estos archivos:**
- `src/main/java/**/controller/*.java` - Validaci�n de entrada
- `src/main/java/**/entity/*.java` - Exposici�n de datos sensibles
- `src/main/resources/application*.properties` - Configuraciones sensibles
- `pom.xml` - Dependencias y versiones

### FASE 3: Validaciones OWASP Top 10

#### 3.1 A01 - Broken Access Control
```bash
# Verificar endpoints sin autenticaci�n
grep -r "@RestController\|@RequestMapping" src/ | grep -v "@PreAuthorize\|@Secured"

# Revisar controladores cr�ticos
find src/ -name "*Controller.java" -exec grep -l "admin\|delete\|create" {} \;
```

#### 3.2 A02 - Cryptographic Failures
```bash
# Buscar hardcoded secrets
grep -r -i "password\|secret\|key\|token" src/ --include="*.java" --include="*.properties"

# Verificar uso de algoritmos d�biles
grep -r "MD5\|SHA1\|DES" src/ --include="*.java"
```

#### 3.3 A03 - Injection
```bash
# Buscar posibles SQL injection
grep -r "createQuery\|createNativeQuery" src/ --include="*.java"

# Verificar validaci�n de entrada
grep -r "@Valid\|@Validated" src/ --include="*.java"
```

#### 3.4 A04 - Insecure Design
```bash
# Revisar l�gica de negocio cr�tica
find src/ -name "*Service.java" -exec grep -l "level\|xp\|streak" {} \;
```

#### 3.5 A05 - Security Misconfiguration
```bash
# Verificar configuraciones de producci�n
grep -r "debug\|trace\|show-sql" src/main/resources/

# Revisar configuraci�n de actuator
grep -r "management.endpoints" src/main/resources/
```

#### 3.6 A06 - Vulnerable Components
```bash
# Listar dependencias con vulnerabilidades conocidas
mvn dependency:tree | grep -E "(SNAPSHOT|BETA|RC|M[0-9])"
```

### FASE 4: Pruebas Espec�ficas de Seguridad

#### 4.1 Validaci�n de DTOs
```bash
# Verificar que todos los DTOs tienen validaciones
find src/ -name "*DTO.java" -exec grep -L "@Valid\|@NotNull\|@Size" {} \;
```

#### 4.2 Verificaci�n de Mappers
```bash
# Asegurar que mappers no expongan datos sensibles
find src/ -name "*Mapper.java" -exec grep -l "password\|token\|secret" {} \;
```

#### 4.3 Configuraci�n de Base de Datos
```bash
# Verificar configuraciones de H2 y PostgreSQL
grep -r "h2.console\|show-sql\|ddl-auto" src/main/resources/
```

## Proceso de Reparaci�n

### Reparaci�n por Tipo de Vulnerabilidad

#### =' Dependencias Vulnerables
1. Identificar versi�n segura m�s reciente
2. Actualizar en `pom.xml`
3. Ejecutar tests completos
4. Verificar compatibilidad

#### =' C�digo Inseguro
1. Aplicar principio de menor privilegio
2. Implementar validaci�n de entrada
3. Sanitizar datos de salida
4. Agregar logging de seguridad

#### =' Configuraci�n Insegura
1. Remover configuraciones de debug en producci�n
2. Configurar HTTPS obligatorio
3. Limitar endpoints de actuator
4. Configurar CORS apropiadamente

### Plantillas de Reparaci�n

#### Template: Actualizaci�n de Dependencia
```xml
<!-- ANTES -->
<dependency>
    <groupId>org.example</groupId>
    <artifactId>vulnerable-lib</artifactId>
    <version>1.0.0</version>
</dependency>

<!-- DESPU�S -->
<dependency>
    <groupId>org.example</groupId>
    <artifactId>vulnerable-lib</artifactId>
    <version>1.2.3</version> <!-- Versi�n segura -->
</dependency>
```

#### Template: Validaci�n de DTO
```java
// ANTES
public class UsuarioDTO {
    private String nombre;
}

// DESPU�S
public class UsuarioDTO {
    @NotBlank(message = "Nombre es obligatorio")
    @Size(min = 2, max = 50, message = "Nombre debe tener entre 2 y 50 caracteres")
    @Pattern(regexp = "^[a-zA-Z������������\\s]+$", message = "Nombre solo puede contener letras")
    private String nombre;
}
```

## Formato de Reporte de Vulnerabilidades

### Estructura del Reporte
```
ID: VULN-YYYY-MM-DD-XXX
Fecha: YYYY-MM-DD HH:MM:SS
Severidad: [CR�TICA|ALTA|MEDIA|BAJA]
Categor�a: [OWASP A01-A10|DEPENDENCY|CONFIG|CODE]
Estado: [DETECTADA|EN_PROGRESO|RESUELTA|ACEPTADA]

Descripci�n:
[Descripci�n detallada de la vulnerabilidad]

Ubicaci�n:
- Archivo: src/main/java/com/ejemplo/Clase.java:123
- Dependencia: org.example:lib:1.0.0
- Configuraci�n: application-prod.properties:45

Impacto:
[Descripci�n del impacto potencial]

CVE/CWE:
- CVE: CVE-2023-XXXXX (si aplica)
- CWE: CWE-XXX (si aplica)

Soluci�n Aplicada:
[Descripci�n de la reparaci�n implementada]

Verificaci�n:
[C�mo se verific� que la vulnerabilidad fue resuelta]

Tiempo de Resoluci�n: X horas/d�as
```

## Archivo de Historial

### Ubicaci�n del Historial
- **Archivo principal**: `.security/vulnerability-history.md`
- **Reportes detallados**: `.security/reports/YYYY-MM-DD/`
- **Configuraci�n**: `.security/security-config.json`

### Comando para Crear Estructura
```bash
mkdir -p .security/reports
touch .security/vulnerability-history.md
touch .security/security-config.json
```

## Automatizaci�n y Programaci�n

### Script de Ejecuci�n Completa
```bash
#!/bin/bash
# security-check.sh

echo "=== INICIANDO VALIDACI�N DE SEGURIDAD ==="
DATE=$(date +%Y-%m-%d)
REPORT_DIR=".security/reports/$DATE"
mkdir -p "$REPORT_DIR"

# FASE 1: Dependencias
echo "1. Analizando dependencias..."
mvn org.sonatype.ossindex.maven:ossindex-maven-plugin:audit > "$REPORT_DIR/dependency-audit.txt"

# FASE 2: C�digo est�tico
echo "2. An�lisis est�tico de c�digo..."
mvn spotbugs:spotbugs
cp target/spotbugsXml.xml "$REPORT_DIR/"

# FASE 3: OWASP Top 10
echo "3. Validaciones OWASP..."
# Ejecutar validaciones espec�ficas...

# FASE 4: Generar reporte
echo "4. Generando reporte consolidado..."
# Consolidar resultados...

echo "=== VALIDACI�N COMPLETADA ==="
echo "Reportes generados en: $REPORT_DIR"
```

### Programaci�n Autom�tica
```bash
# Agregar a crontab para ejecuci�n semanal
0 2 * * 1 /path/to/security-check.sh
```

## Lista de Verificaci�n Final

###  Antes de Completar la Validaci�n
- [ ] Todas las herramientas ejecutadas sin errores
- [ ] Vulnerabilidades cr�ticas y altas resueltas
- [ ] Tests de seguridad pasando
- [ ] Documentaci�n actualizada
- [ ] Historial de vulnerabilidades actualizado
- [ ] Configuraciones de producci�n validadas
- [ ] Dependencias actualizadas a versiones seguras

### =� M�tricas a Reportar
- N�mero total de vulnerabilidades encontradas
- Vulnerabilidades por severidad (Cr�tica/Alta/Media/Baja)
- Tiempo promedio de resoluci�n
- Vulnerabilidades recurrentes
- Porcentaje de cobertura de an�lisis

## Contactos de Escalamiento

### En caso de vulnerabilidades cr�ticas:
1. **Inmediato**: Deshabilitar funcionalidad afectada
2. **1 hora**: Notificar a stakeholders
3. **4 horas**: Implementar fix temporal
4. **24 horas**: Implementar soluci�n permanente

---

**Nota**: Este comando debe ejecutarse antes de cada release y mensualmente para mantener la seguridad del sistema.

## Pr�xima Actualizaci�n
- **Fecha programada**: 30 d�as desde �ltima ejecuci�n
- **Trigger**: Cambios en dependencias o c�digo cr�tico
- **Responsable**: Equipo de desarrollo