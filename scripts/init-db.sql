-- Script de inicialización de base de datos PostgreSQL para producción

-- Crear schema si no existe
CREATE SCHEMA IF NOT EXISTS taskmanager;

-- Establecer schema por defecto
SET search_path TO taskmanager;

-- Configurar timezone
SET timezone = 'UTC';

-- Comentarios sobre las tablas que se crearán automáticamente por Hibernate
COMMENT ON SCHEMA taskmanager IS 'Schema para Task Manager Gamificado';

-- Función para limpiar historial antiguo (más de 12 meses)
CREATE OR REPLACE FUNCTION cleanup_old_history()
RETURNS void AS $$
BEGIN
    DELETE FROM historial_tareas 
    WHERE fecha_completacion < NOW() - INTERVAL '12 months';
    
    RAISE NOTICE 'Limpieza de historial completada: % registros eliminados', ROW_COUNT;
END;
$$ LANGUAGE plpgsql;

-- Función para calcular XP requerido por nivel
CREATE OR REPLACE FUNCTION calculate_xp_required(nivel INTEGER)
RETURNS INTEGER AS $$
BEGIN
    -- Fórmula: Nivel 1 = 25 XP, escalamiento gradual
    -- Nivel n = 25 * (1.5 ^ (n-1))
    RETURN FLOOR(25 * POWER(1.5, nivel - 1));
END;
$$ LANGUAGE plpgsql;

-- Índices adicionales para optimización (se aplicarán después de crear tablas)
-- Estas se ejecutarán automáticamente cuando Hibernate cree las tablas

-- Nota: Las tablas se crearán automáticamente por Hibernate/JPA
-- Este script solo prepara la base de datos con funciones auxiliares