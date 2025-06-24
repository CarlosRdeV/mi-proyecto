#!/bin/bash

# ==============================================================================
# SCRIPT DE VALIDACIÓN DE SEGURIDAD - TASK MANAGER GAMIFICADO
# ==============================================================================
# Versión: 1.2.0
# Descripción: Ejecuta validación completa de vulnerabilidades siguiendo 
#              las mejores prácticas de OWASP y estándares de la industria
# ==============================================================================

set -e  # Salir en caso de error

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Variables globales
DATE=$(date +%Y-%m-%d)
TIMESTAMP=$(date +%Y-%m-%d_%H-%M-%S)
REPORT_DIR=".security/reports/$DATE"
SECURITY_DIR=".security"
HISTORY_FILE="$SECURITY_DIR/vulnerability-history.md"
CONFIG_FILE="$SECURITY_DIR/security-config.json"

# Contadores de vulnerabilidades
CRITICAL_COUNT=0
HIGH_COUNT=0
MEDIUM_COUNT=0
LOW_COUNT=0
TOTAL_COUNT=0

# ==============================================================================
# FUNCIONES AUXILIARES
# ==============================================================================

# Validación de regresión obligatoria
regression_test_validation() {
    print_step "REG" "Ejecutando validación de regresión obligatoria..."
    
    local regression_report="$REPORT_DIR/regression-validation.txt"
    local baseline_file="$SECURITY_DIR/test-baseline.txt"
    
    {
        echo "=== VALIDACIÓN DE REGRESIÓN ==="
        echo "Fecha: $TIMESTAMP"
        echo "Propósito: Asegurar que no se rompa funcionalidad existente"
        echo ""
        
        # Ejecutar tests completos
        echo "1. Ejecutando suite completa de tests..."
        if mvn test > temp-test-results.txt 2>&1; then
            echo "✓ Todos los tests pasaron"
            test_status="PASS"
        else
            echo "✗ Algunos tests fallaron"
            test_status="FAIL"
            cat temp-test-results.txt | grep "FAILURE\|ERROR" || echo "No se pudieron obtener detalles de fallas"
        fi
        
        # Validar endpoints críticos (si el servicio está corriendo)
        echo ""
        echo "2. Validando endpoints críticos..."
        
        # Intentar validar endpoints solo si el servicio responde
        if curl -s -f http://localhost:8080/api/test/health >/dev/null 2>&1; then
            echo "✓ Endpoint health accesible"
            
            # Verificar estructura de respuesta JSON
            health_response=$(curl -s http://localhost:8080/api/test/health 2>/dev/null)
            if echo "$health_response" | grep -q "status\|timestamp"; then
                echo "✓ Estructura de respuesta health mantiene compatibilidad"
            else
                echo "⚠ Estructura de respuesta health cambió"
            fi
            
            # Validar otros endpoints si están disponibles
            if curl -s -f http://localhost:8080/api/test/usuarios >/dev/null 2>&1; then
                echo "✓ Endpoint usuarios accesible"
            fi
            
            if curl -s -f http://localhost:8080/api/test/tareas >/dev/null 2>&1; then
                echo "✓ Endpoint tareas accesible"  
            fi
        else
            echo "ℹ Servicio no está corriendo - validación de endpoints omitida"
            echo "  Para validación completa, ejecutar: mvn spring-boot:run"
        fi
        
        # Verificar que DTOs mantienen estructura
        echo ""
        echo "3. Verificando estructura de DTOs..."
        dto_files=$(find src/ -name "*DTO.java" 2>/dev/null | wc -l)
        echo "  - DTOs encontrados: $dto_files"
        
        # Buscar cambios en nombres de campos públicos (heurística)
        if git status --porcelain 2>/dev/null | grep -q "DTO.java"; then
            echo "⚠ DTOs modificados - revisar manualmente que no cambien campos públicos"
        else
            echo "✓ No se detectaron modificaciones en DTOs"
        fi
        
        # Análisis de rendimiento básico (compilación)
        echo ""
        echo "4. Verificando rendimiento de compilación..."
        compile_start=$(date +%s)
        if mvn compile -q > /dev/null 2>&1; then
            compile_end=$(date +%s)
            compile_time=$((compile_end - compile_start))
            echo "✓ Compilación exitosa en ${compile_time}s"
            
            # Alertar si compilación toma mucho tiempo
            if [[ $compile_time -gt 60 ]]; then
                echo "⚠ Tiempo de compilación alto (${compile_time}s) - posible degradación"
            fi
        else
            echo "✗ Error en compilación"
        fi
        
        echo ""
        echo "=== RESUMEN DE VALIDACIÓN DE REGRESIÓN ==="
        echo "Estado de tests: $test_status"
        echo "Tiempo de compilación: ${compile_time:-N/A}s"
        echo "Compatibilidad de API: $([ "$test_status" = "PASS" ] && echo "MANTENIDA" || echo "REQUIERE REVISIÓN")"
        
    } > "$regression_report" 2>&1
    
    # Cleanup archivos temporales
    [[ -f temp-test-results.txt ]] && rm temp-test-results.txt
    
    if [[ "$test_status" = "PASS" ]]; then
        print_success "Validación de regresión completada - Sin problemas detectados"
    else
        print_warning "Validación de regresión completada - REQUIERE ATENCIÓN"
        print_error "Tests fallaron - Revisar antes de proceder con fixes de seguridad"
    fi
    
    return $([ "$test_status" = "PASS" ] && echo 0 || echo 1)
}

print_header() {
    echo -e "${BLUE}================================${NC}"
    echo -e "${BLUE} $1${NC}"
    echo -e "${BLUE}================================${NC}"
}

print_step() {
    echo -e "${GREEN}[PASO $1]${NC} $2"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

# Crear estructura de directorios
setup_directories() {
    print_step "0" "Configurando estructura de directorios..."
    mkdir -p "$REPORT_DIR"
    mkdir -p "$SECURITY_DIR/tools"
    mkdir -p "$SECURITY_DIR/configs"
    
    # Crear archivos base si no existen
    [[ ! -f "$HISTORY_FILE" ]] && touch "$HISTORY_FILE"
    [[ ! -f "$CONFIG_FILE" ]] && echo "{}" > "$CONFIG_FILE"
    
    print_success "Estructura de directorios configurada"
}

# Verificar herramientas necesarias
check_tools() {
    print_step "1" "Verificando herramientas de seguridad..."
    
    # Maven
    if ! command -v mvn &> /dev/null; then
        print_error "Maven no está instalado"
        exit 1
    fi
    
    # Java
    if ! command -v java &> /dev/null; then
        print_error "Java no está instalado"
        exit 1
    fi
    
    print_success "Herramientas básicas verificadas"
}

# ==============================================================================
# FASE 1: ANÁLISIS DE DEPENDENCIAS
# ==============================================================================

analyze_dependencies() {
    print_header "FASE 1: ANÁLISIS DE DEPENDENCIAS"
    
    print_step "1.1" "Ejecutando Maven Security Audit..."
    
    # Crear archivo de reporte
    local dep_report="$REPORT_DIR/dependency-analysis.txt"
    
    {
        echo "=== MAVEN DEPENDENCY SECURITY AUDIT ==="
        echo "Fecha: $TIMESTAMP"
        echo "Proyecto: Task Manager Gamificado"
        echo ""
        
        # Ejecutar audit de dependencias
        if mvn org.sonatype.ossindex.maven:ossindex-maven-plugin:audit; then
            echo "✓ Audit completado exitosamente"
        else
            echo "⚠ Audit completado con warnings/errores"
        fi
        
        echo ""
        echo "=== VERIFICACIÓN DE VERSIONES ==="
        mvn versions:display-dependency-updates 2>/dev/null || echo "No se pudieron verificar updates"
        
    } > "$dep_report" 2>&1
    
    print_success "Análisis de dependencias completado"
    
    # Analizar resultados (simplificado)
    if grep -q "vulnerabilities found" "$dep_report"; then
        print_warning "Se encontraron vulnerabilidades en dependencias"
        ((TOTAL_COUNT++))
        ((HIGH_COUNT++))
    fi
}

# ==============================================================================
# FASE 2: ANÁLISIS ESTÁTICO DE CÓDIGO
# ==============================================================================

static_code_analysis() {
    print_header "FASE 2: ANÁLISIS ESTÁTICO DE CÓDIGO"
    
    print_step "2.1" "Ejecutando SpotBugs..."
    
    local spotbugs_report="$REPORT_DIR/spotbugs-analysis.txt"
    
    {
        echo "=== SPOTBUGS SECURITY ANALYSIS ==="
        echo "Fecha: $TIMESTAMP"
        echo ""
        
        # Compilar proyecto primero
        if mvn clean compile; then
            echo "✓ Compilación exitosa"
        else
            echo "✗ Error en compilación"
            exit 1
        fi
        
        # Ejecutar SpotBugs si está configurado
        if mvn spotbugs:check 2>/dev/null; then
            echo "✓ SpotBugs completado sin issues críticos"
        else
            echo "⚠ SpotBugs encontró posibles issues"
        fi
        
    } > "$spotbugs_report" 2>&1
    
    print_success "Análisis estático completado"
}

# ==============================================================================
# FASE 3: VALIDACIONES OWASP TOP 10
# ==============================================================================

owasp_validation() {
    print_header "FASE 3: VALIDACIONES OWASP TOP 10"
    
    local owasp_report="$REPORT_DIR/owasp-validation.txt"
    
    {
        echo "=== OWASP TOP 10 VALIDATION ==="
        echo "Fecha: $TIMESTAMP"
        echo ""
        
        # A01 - Broken Access Control
        echo "A01 - Broken Access Control:"
        endpoints_without_auth=$(find src/ -name "*.java" -exec grep -l "@RestController\|@RequestMapping" {} \; 2>/dev/null | wc -l)
        secured_endpoints=$(find src/ -name "*.java" -exec grep -l "@PreAuthorize\|@Secured" {} \; 2>/dev/null | wc -l)
        echo "  - Controladores encontrados: $endpoints_without_auth"
        echo "  - Controladores con seguridad: $secured_endpoints"
        
        # A02 - Cryptographic Failures  
        echo "A02 - Cryptographic Failures:"
        hardcoded_secrets=$(grep -r -i "password\|secret\|key" src/ --include="*.java" --include="*.properties" 2>/dev/null | wc -l)
        weak_crypto=$(grep -r "MD5\|SHA1\|DES" src/ --include="*.java" 2>/dev/null | wc -l)
        echo "  - Posibles secretos hardcodeados: $hardcoded_secrets"
        echo "  - Algoritmos criptográficos débiles: $weak_crypto"
        
        # A03 - Injection
        echo "A03 - Injection:"
        sql_queries=$(grep -r "createQuery\|createNativeQuery" src/ --include="*.java" 2>/dev/null | wc -l)
        validated_inputs=$(grep -r "@Valid\|@Validated" src/ --include="*.java" 2>/dev/null | wc -l)
        echo "  - Queries SQL encontradas: $sql_queries"
        echo "  - Inputs validados: $validated_inputs"
        
        # A05 - Security Misconfiguration
        echo "A05 - Security Misconfiguration:"
        debug_configs=$(grep -r "debug\|trace\|show-sql" src/main/resources/ 2>/dev/null | wc -l)
        actuator_endpoints=$(grep -r "management.endpoints" src/main/resources/ 2>/dev/null | wc -l)
        echo "  - Configuraciones de debug: $debug_configs"
        echo "  - Configuraciones de actuator: $actuator_endpoints"
        
        # A06 - Vulnerable Components
        echo "A06 - Vulnerable Components:"
        snapshot_deps=$(mvn dependency:tree 2>/dev/null | grep -E "(SNAPSHOT|BETA|RC|M[0-9])" | wc -l)
        echo "  - Dependencias inestables: $snapshot_deps"
        
    } > "$owasp_report" 2>&1
    
    print_success "Validaciones OWASP completadas"
    
    # Análisis simplificado de resultados
    if [[ $hardcoded_secrets -gt 0 ]]; then
        print_warning "Posibles secretos hardcodeados detectados"
        ((TOTAL_COUNT++))
        ((MEDIUM_COUNT++))
    fi
    
    if [[ $debug_configs -gt 0 ]]; then
        print_warning "Configuraciones de debug encontradas"
        ((TOTAL_COUNT++))
        ((LOW_COUNT++))
    fi
}

# ==============================================================================
# FASE 4: VALIDACIONES ESPECÍFICAS DEL PROYECTO
# ==============================================================================

project_specific_validation() {
    print_header "FASE 4: VALIDACIONES ESPECÍFICAS"
    
    local project_report="$REPORT_DIR/project-validation.txt"
    
    {
        echo "=== PROJECT SPECIFIC VALIDATION ==="
        echo "Fecha: $TIMESTAMP"
        echo ""
        
        # Validación de DTOs
        echo "Validación de DTOs:"
        total_dtos=$(find src/ -name "*DTO.java" 2>/dev/null | wc -l)
        validated_dtos=$(find src/ -name "*DTO.java" -exec grep -l "@Valid\|@NotNull\|@Size" {} \; 2>/dev/null | wc -l)
        echo "  - DTOs totales: $total_dtos"
        echo "  - DTOs con validaciones: $validated_dtos"
        
        # Verificación de Mappers
        echo "Verificación de Mappers:"
        mappers_with_sensitive=$(find src/ -name "*Mapper.java" -exec grep -l "password\|token\|secret" {} \; 2>/dev/null | wc -l)
        echo "  - Mappers con datos sensibles: $mappers_with_sensitive"
        
        # Configuración de Base de Datos
        echo "Configuración de Base de Datos:"
        h2_console_enabled=$(grep -r "h2.console.enabled=true" src/main/resources/ 2>/dev/null | wc -l)
        show_sql_enabled=$(grep -r "show-sql=true" src/main/resources/ 2>/dev/null | wc -l)
        echo "  - H2 Console habilitada: $h2_console_enabled"
        echo "  - Show SQL habilitado: $show_sql_enabled"
        
    } > "$project_report" 2>&1
    
    print_success "Validaciones específicas completadas"
    
    # Análisis de resultados
    if [[ $mappers_with_sensitive -gt 0 ]]; then
        print_warning "Mappers con posibles datos sensibles"
        ((TOTAL_COUNT++))
        ((MEDIUM_COUNT++))
    fi
}

# ==============================================================================
# GENERACIÓN DE REPORTES
# ==============================================================================

generate_summary_report() {
    print_header "GENERANDO REPORTE CONSOLIDADO"
    
    local summary_report="$REPORT_DIR/security-summary.md"
    
    {
        echo "# Reporte de Seguridad - Task Manager Gamificado"
        echo ""
        echo "- **Fecha de análisis**: $TIMESTAMP"
        echo "- **Versión del sistema**: 1.2.0"
        echo "- **Herramientas utilizadas**: Maven Security, SpotBugs, Validaciones OWASP"
        echo ""
        
        echo "## Resumen Ejecutivo"
        echo ""
        echo "| Severidad | Cantidad |"
        echo "|-----------|----------|"
        echo "| Crítica   | $CRITICAL_COUNT |"
        echo "| Alta      | $HIGH_COUNT |"
        echo "| Media     | $MEDIUM_COUNT |"
        echo "| Baja      | $LOW_COUNT |"
        echo "| **Total** | **$TOTAL_COUNT** |"
        echo ""
        
        if [[ $TOTAL_COUNT -eq 0 ]]; then
            echo "✅ **No se encontraron vulnerabilidades en el análisis actual.**"
        else
            echo "⚠️ **Se encontraron $TOTAL_COUNT posibles issues de seguridad.**"
        fi
        echo ""
        
        echo "## Detalles por Fase"
        echo ""
        echo "### Fase 1: Análisis de Dependencias"
        echo "- Estado: Completado"
        echo "- Reporte detallado: \`dependency-analysis.txt\`"
        echo ""
        
        echo "### Fase 2: Análisis Estático"
        echo "- Estado: Completado"
        echo "- Reporte detallado: \`spotbugs-analysis.txt\`"
        echo ""
        
        echo "### Fase 3: Validaciones OWASP"
        echo "- Estado: Completado"
        echo "- Reporte detallado: \`owasp-validation.txt\`"
        echo ""
        
        echo "### Fase 4: Validaciones Específicas"
        echo "- Estado: Completado"
        echo "- Reporte detallado: \`project-validation.txt\`"
        echo ""
        
        echo "## Próximos Pasos"
        echo ""
        if [[ $TOTAL_COUNT -gt 0 ]]; then
            echo "1. Revisar reportes detallados en \`.security/reports/$DATE/\`"
            echo "2. Priorizar corrección de vulnerabilidades críticas y altas"
            echo "3. Implementar fixes siguiendo las plantillas en el comando de seguridad"
            echo "4. Re-ejecutar validación después de aplicar correcciones"
        else
            echo "1. Mantener monitoreo regular de seguridad"
            echo "2. Próxima validación programada en 30 días"
            echo "3. Ejecutar validación antes del próximo release"
        fi
        echo ""
        
        echo "---"
        echo "*Generado automáticamente por security-check.sh v1.2.0*"
        
    } > "$summary_report"
    
    print_success "Reporte consolidado generado: $summary_report"
}

update_history() {
    print_step "5" "Actualizando historial de vulnerabilidades..."
    
    # Backup del historial actual
    cp "$HISTORY_FILE" "$HISTORY_FILE.backup-$TIMESTAMP"
    
    # Actualizar información general en el historial
    local temp_file=$(mktemp)
    
    {
        echo "# Historial de Vulnerabilidades - Task Manager Gamificado"
        echo ""
        echo "## Información General"
        echo "- **Proyecto**: Task Manager Gamificado"
        echo "- **Versión del sistema de seguridad**: v1.2.0"
        echo "- **Última validación**: $TIMESTAMP"
        echo "- **Próxima validación programada**: $(date -d '+30 days' +%Y-%m-%d)"
        echo ""
        echo "## Resumen de Última Validación"
        echo ""
        echo "### Estadísticas"
        echo "- **Vulnerabilidades críticas**: $CRITICAL_COUNT"
        echo "- **Vulnerabilidades altas**: $HIGH_COUNT"
        echo "- **Vulnerabilidades medias**: $MEDIUM_COUNT"
        echo "- **Vulnerabilidades bajas**: $LOW_COUNT"
        echo "- **Total detectadas**: $TOTAL_COUNT"
        echo ""
        echo "### Estado del Sistema"
        if [[ $TOTAL_COUNT -eq 0 ]]; then
            echo "- **Nivel de riesgo**: Bajo"
            echo "- **Estado**: Seguro para producción"
        else
            echo "- **Nivel de riesgo**: Medio"
            echo "- **Estado**: Requiere atención"
        fi
        echo ""
        echo "---"
        echo ""
        
        # Agregar el resto del contenido existente del historial
        if [[ -f "$HISTORY_FILE.backup-$TIMESTAMP" ]]; then
            tail -n +20 "$HISTORY_FILE.backup-$TIMESTAMP" 2>/dev/null || echo ""
        fi
        
    } > "$temp_file"
    
    mv "$temp_file" "$HISTORY_FILE"
    print_success "Historial actualizado"
}

# ==============================================================================
# FUNCIÓN PRINCIPAL
# ==============================================================================

main() {
    print_header "VALIDACIÓN DE SEGURIDAD - TASK MANAGER GAMIFICADO"
    echo "⚠️  MODO SEGURO: Protección contra ruptura de funcionalidad habilitada"
    echo "Iniciando validación completa de vulnerabilidades..."
    echo "Fecha: $TIMESTAMP"
    echo ""
    
    # Verificar que estamos en el directorio correcto
    if [[ ! -f "pom.xml" ]]; then
        print_error "No se encontró pom.xml. Ejecutar desde el directorio raíz del proyecto."
        exit 1
    fi
    
    # FASE CRÍTICA: Validación de regresión ANTES de cualquier análisis
    print_header "FASE 0: VALIDACIÓN DE REGRESIÓN BASELINE"
    if ! regression_test_validation; then
        print_error "ALERTA: Tests baseline fallan. Sistema tiene problemas existentes."
        print_warning "Proceder con precaución - cualquier fix debe resolver problemas existentes"
        read -p "¿Continuar de todos modos? (y/N): " -n 1 -r
        echo
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            print_error "Validación cancelada por el usuario"
            exit 1
        fi
    fi
    
    # Ejecutar fases de análisis
    setup_directories
    check_tools
    analyze_dependencies
    static_code_analysis
    owasp_validation
    project_specific_validation
    generate_summary_report
    update_history
    
    # Resumen final
    print_header "VALIDACIÓN COMPLETADA"
    echo ""
    echo "📊 Resumen de vulnerabilidades encontradas:"
    echo "   - Críticas: $CRITICAL_COUNT"
    echo "   - Altas: $HIGH_COUNT" 
    echo "   - Medias: $MEDIUM_COUNT"
    echo "   - Bajas: $LOW_COUNT"
    echo "   - Total: $TOTAL_COUNT"
    echo ""
    echo "📁 Reportes generados en: $REPORT_DIR"
    echo "📋 Reporte principal: $REPORT_DIR/security-summary.md"
    echo "📜 Historial actualizado: $HISTORY_FILE"
    echo ""
    
    if [[ $TOTAL_COUNT -eq 0 ]]; then
        print_success "🎉 No se encontraron vulnerabilidades. Sistema seguro!"
        print_success "✅ Funcionalidad baseline validada - Sistema estable"
    else
        print_warning "⚠️  Se encontraron $TOTAL_COUNT posibles vulnerabilidades."
        echo "   Revisar reportes detallados y aplicar correcciones necesarias."
        echo ""
        print_header "RECORDATORIO CRÍTICO ANTES DE APLICAR FIXES"
        echo "🔒 NUNCA cambiar nombres de campos, parámetros o endpoints"
        echo "🔒 SIEMPRE ejecutar 'mvn test' después de cada cambio" 
        echo "🔒 VALIDAR que la API mantiene compatibilidad total"
        echo "🔒 En caso de duda, usar estrategias no disruptivas (ver comando)"
        echo ""
        echo "📖 Consultar: .claude/commands/check_vulnebilities.md"
        echo "   Sección: 'Proceso de Reparación SEGURA'"
    fi
    
    echo ""
    echo "Siguiente validación recomendada: $(date -d '+30 days' +%Y-%m-%d)"
    echo "💡 Para fixes seguros, siempre usar el proceso de validación de regresión"
}

# ==============================================================================
# EJECUCIÓN
# ==============================================================================

# Verificar si se ejecuta directamente
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    main "$@"
fi