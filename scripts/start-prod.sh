#!/bin/bash

# Script para iniciar la aplicación en modo PRODUCCIÓN

echo "🏭 Iniciando Task Manager Gamificado en modo PRODUCCIÓN..."

# Verificar variables de entorno requeridas
required_vars=("DATABASE_URL" "DATABASE_USERNAME" "DATABASE_PASSWORD")
missing_vars=()

for var in "${required_vars[@]}"; do
    if [ -z "${!var}" ]; then
        missing_vars+=("$var")
    fi
done

if [ ${#missing_vars[@]} -ne 0 ]; then
    echo "❌ Error: Las siguientes variables de entorno son requeridas para producción:"
    printf ' - %s\n' "${missing_vars[@]}"
    echo ""
    echo "Ejemplo de configuración:"
    echo "export DATABASE_URL=jdbc:postgresql://localhost:5432/taskmanager_prod"
    echo "export DATABASE_USERNAME=taskmanager_user"
    echo "export DATABASE_PASSWORD=secure_password"
    exit 1
fi

# Establecer variables de entorno para producción
export SPRING_PROFILES_ACTIVE=prod
export JAVA_OPTS="-Xms2048m -Xmx4096m -XX:+UseG1GC -XX:+UseStringDeduplication"

# Crear directorio de logs
mkdir -p ./logs

# Compilar con optimizaciones de producción
echo "📦 Compilando proyecto para producción..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
    
    # Verificar conexión a base de datos (opcional)
    echo "🔍 Verificando configuración..."
    
    # Ejecutar aplicación
    echo "▶️  Ejecutando aplicación en http://localhost:8080"
    echo "📊 Health check disponible en http://localhost:8080/actuator/health"
    echo "📝 Logs en ./logs/taskmanager.log"
    echo ""
    echo "⚠️  MODO PRODUCCIÓN ACTIVO - Consola H2 DESHABILITADA"
    echo ""
    echo "Para detener la aplicación presiona Ctrl+C"
    echo ""
    
    java $JAVA_OPTS -jar target/gamified-taskmanager-1.0.0.jar --spring.profiles.active=prod
else
    echo "❌ Error en la compilación"
    exit 1
fi