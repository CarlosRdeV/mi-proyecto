#!/bin/bash

# Script para ejecutar tests con diferentes configuraciones

echo "🧪 Ejecutando tests del Task Manager Gamificado..."

# Función para mostrar resultados
show_results() {
    if [ $1 -eq 0 ]; then
        echo "✅ $2 - PASARON"
    else
        echo "❌ $2 - FALLARON"
        FAILED_TESTS=true
    fi
    echo ""
}

FAILED_TESTS=false

# Tests unitarios
echo "📝 Ejecutando tests unitarios..."
mvn test -Dtest="**/*Test"
show_results $? "Tests unitarios"

# Tests de integración
echo "🔗 Ejecutando tests de integración..."
mvn test -Dtest="**/*IntegrationTest"
show_results $? "Tests de integración"

# Tests completos con reporte
echo "📊 Ejecutando suite completa de tests..."
mvn clean test
show_results $? "Suite completa de tests"

# Generar reporte de cobertura (si está configurado)
echo "📈 Generando reporte de cobertura..."
mvn jacoco:report 2>/dev/null
if [ $? -eq 0 ]; then
    echo "✅ Reporte de cobertura generado en target/site/jacoco/"
else
    echo "⚠️  Reporte de cobertura no disponible (requiere configuración de JaCoCo)"
fi

echo ""
if [ "$FAILED_TESTS" = true ]; then
    echo "❌ Algunos tests fallaron. Revisar los logs arriba."
    exit 1
else
    echo "🎉 ¡Todos los tests pasaron exitosamente!"
    exit 0
fi