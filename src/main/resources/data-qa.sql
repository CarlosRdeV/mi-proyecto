-- Datos específicos para ambiente QA
-- Incluye datos de prueba adicionales para testing

-- Insertar tareas predefinidas (mismo que data.sql)
INSERT INTO tareas (tipo_tarea, nombre, descripcion, dificultad, xp_base, repetible, activa, fecha_creacion) VALUES
('NORMAL', 'Beber un vaso de agua', 'Hidratarse bebiendo al menos 250ml de agua', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Dar un paseo de 5 minutos', 'Caminar durante 5 minutos para activar el cuerpo', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Pasear al perro', 'Sacar a pasear a tu mascota por al menos 10 minutos', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Hacer la cama', 'Ordenar y tender la cama después de levantarse', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Lavar los platos después de comer', 'Limpiar los utensilios usados en la comida', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Leer 10 minutos', 'Dedicar tiempo a la lectura de cualquier material', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Escuchar música mientras haces ejercicio ligero', 'Combinar música con movimiento suave', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),

('NORMAL', 'Hacer ejercicio 30 minutos', 'Realizar actividad física moderada por media hora', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Cocinar una comida completa', 'Preparar una comida balanceada desde cero', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Limpiar una habitación de la casa', 'Ordenar y limpiar completamente un espacio', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Estudiar/trabajar 1 hora concentrado', 'Dedicar tiempo focalizado al trabajo o estudio', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Llamar a un familiar o amigo', 'Conectar socialmente con alguien importante', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Organizar un espacio de trabajo', 'Ordenar y optimizar tu área de trabajo', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Meditar 15 minutos', 'Practicar mindfulness o meditación', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),

('NORMAL', 'Completar un proyecto importante del trabajo/estudio', 'Finalizar una tarea significativa pendiente', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Hacer ejercicio intenso por 1 hora', 'Entrenamiento de alta intensidad', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Limpiar toda la casa', 'Limpieza profunda de todo el hogar', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Aprender algo nuevo por 2+ horas', 'Dedicar tiempo extenso a adquirir nuevos conocimientos', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Completar una tarea que has estado posponiendo', 'Abordar esa tarea que has evitado hacer', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Hacer una actividad social fuera de tu zona de confort', 'Desafiarte socialmente con algo nuevo', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP);

-- Usuarios de prueba para QA
INSERT INTO usuarios (nombre, nivel, experiencia, streak_actual, fecha_creacion, fecha_ultimo_login) VALUES
('UsuarioQA1', 1, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('UsuarioQA2', 3, 75, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('UsuarioQA3', 5, 200, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TesterQA', 10, 500, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Eventos para QA
INSERT INTO eventos (nombre, descripcion, multiplicador, fecha_inicio, fecha_fin, activo, fecha_creacion) VALUES
('Evento QA Activo', 'Evento de prueba con 15% de XP extra', 1.15, 
 DATEADD('DAY', -2, CURRENT_TIMESTAMP), DATEADD('DAY', 15, CURRENT_TIMESTAMP), true, CURRENT_TIMESTAMP),
('Evento QA Futuro', 'Evento de prueba programado para el futuro', 1.25, 
 DATEADD('DAY', 10, CURRENT_TIMESTAMP), DATEADD('DAY', 20, CURRENT_TIMESTAMP), true, CURRENT_TIMESTAMP),
('Evento QA Pasado', 'Evento de prueba que ya terminó', 1.2, 
 DATEADD('DAY', -20, CURRENT_TIMESTAMP), DATEADD('DAY', -10, CURRENT_TIMESTAMP), true, CURRENT_TIMESTAMP);

-- Tareas especiales para QA
INSERT INTO tareas (tipo_tarea, nombre, descripcion, dificultad, xp_base, repetible, activa, fecha_creacion, fecha_limite, una_vez_por_usuario) VALUES
('ESPECIAL', 'Tarea Especial QA Vigente', 'Tarea especial para testing con fecha límite', 'ESPECIAL', 150, false, true, CURRENT_TIMESTAMP, 
 DATEADD('DAY', 30, CURRENT_TIMESTAMP), true),
('ESPECIAL', 'Tarea Especial QA Vencida', 'Tarea especial que ya venció', 'ESPECIAL', 200, false, true, CURRENT_TIMESTAMP, 
 DATEADD('DAY', -5, CURRENT_TIMESTAMP), true);

-- Historial de ejemplo para testing
INSERT INTO historial_tareas (usuario_id, tarea_id, fecha_completacion, xp_ganado, multiplicador_streak, multiplicador_evento, multiplicador_total) VALUES
(2, 1, DATEADD('DAY', -1, CURRENT_TIMESTAMP), 11, 1.0, 1.15, 1.15),
(2, 8, DATEADD('DAY', -1, CURRENT_TIMESTAMP), 29, 1.0, 1.15, 1.15),
(3, 2, DATEADD('HOUR', -12, CURRENT_TIMESTAMP), 12, 1.05, 1.15, 1.21),
(3, 14, DATEADD('HOUR', -6, CURRENT_TIMESTAMP), 61, 1.05, 1.15, 1.21),
(4, 1, DATEADD('MINUTE', -30, CURRENT_TIMESTAMP), 13, 1.25, 1.15, 1.44);