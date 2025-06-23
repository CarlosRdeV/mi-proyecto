# Dockerfile multi-stage para Task Manager Gamificado

# Etapa 1: Build
FROM openjdk:17-jdk-slim as build

WORKDIR /app

# Copiar archivos de configuración Maven
COPY pom.xml .
COPY src ./src

# Instalar Maven
RUN apt-get update && apt-get install -y maven

# Compilar aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM openjdk:17-jre-slim

WORKDIR /app

# Crear usuario no-root para seguridad
RUN groupadd -r taskmanager && useradd -r -g taskmanager taskmanager

# Copiar JAR desde etapa build
COPY --from=build /app/target/gamified-taskmanager-1.0.0.jar app.jar

# Crear directorios necesarios
RUN mkdir -p logs data && chown -R taskmanager:taskmanager /app

# Cambiar a usuario no-root
USER taskmanager

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseContainerSupport"

# Puerto expuesto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando de inicio
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]