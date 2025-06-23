#!/bin/bash

# Script para iniciar la aplicación en modo DESARROLLO

echo "🚀 Iniciando Task Manager Gamificado en modo DESARROLLO..."

# Establecer variables de entorno para desarrollo
export SPRING_PROFILES_ACTIVE=dev
export JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# Limpiar y compilar
echo "📦 Compilando proyecto..."
mvn clean compile

# Ejecutar aplicación
echo "▶️  Ejecutando aplicación en http://localhost:8080"
echo "🗄️  Consola H2 disponible en http://localhost:8080/h2-console"
echo "📊 Actuator endpoints en http://localhost:8080/actuator"
echo ""
echo "Para detener la aplicación presiona Ctrl+C"
echo ""

mvn spring-boot:run -Dspring-boot.run.profiles=dev