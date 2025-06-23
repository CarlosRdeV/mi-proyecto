#!/bin/bash

# Script para iniciar la aplicación en modo QA

echo "🧪 Iniciando Task Manager Gamificado en modo QA..."

# Crear directorio de datos si no existe
mkdir -p ./data

# Establecer variables de entorno para QA
export SPRING_PROFILES_ACTIVE=qa
export JAVA_OPTS="-Xms1024m -Xmx2048m -XX:+UseG1GC"

# Limpiar y compilar
echo "📦 Compilando proyecto..."
mvn clean compile

# Ejecutar tests antes de iniciar QA
echo "🧪 Ejecutando tests..."
mvn test

if [ $? -eq 0 ]; then
    echo "✅ Tests pasaron correctamente"
    
    # Ejecutar aplicación
    echo "▶️  Ejecutando aplicación en http://localhost:8081"
    echo "🗄️  Consola H2 disponible en http://localhost:8081/h2-console"
    echo "📊 Actuator endpoints en http://localhost:8081/actuator/health"
    echo "💾 Base de datos persistente en ./data/taskmanager_qa.mv.db"
    echo ""
    echo "Para detener la aplicación presiona Ctrl+C"
    echo ""
    
    mvn spring-boot:run -Dspring-boot.run.profiles=qa
else
    echo "❌ Tests fallaron. No se iniciará QA."
    exit 1
fi