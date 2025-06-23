-- Insertar tareas predefinidas

-- Tareas Fáciles (10 XP)
INSERT INTO tareas (tipo_tarea, nombre, descripcion, dificultad, xp_base, repetible, activa, fecha_creacion) VALUES
('NORMAL', 'Beber un vaso de agua', 'Hidratarse bebiendo al menos 250ml de agua', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Dar un paseo de 5 minutos', 'Caminar durante 5 minutos para activar el cuerpo', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Pasear al perro', 'Sacar a pasear a tu mascota por al menos 10 minutos', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Hacer la cama', 'Ordenar y tender la cama después de levantarse', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Lavar los platos después de comer', 'Limpiar los utensilios usados en la comida', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Leer 10 minutos', 'Dedicar tiempo a la lectura de cualquier material', 'FACIL', 10, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Escuchar música mientras haces ejercicio ligero', 'Combinar música con movimiento suave', 'FACIL', 10, true, true, CURRENT_TIMESTAMP);

-- Tareas Medias (25 XP)
INSERT INTO tareas (tipo_tarea, nombre, descripcion, dificultad, xp_base, repetible, activa, fecha_creacion) VALUES
('NORMAL', 'Hacer ejercicio 30 minutos', 'Realizar actividad física moderada por media hora', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Cocinar una comida completa', 'Preparar una comida balanceada desde cero', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Limpiar una habitación de la casa', 'Ordenar y limpiar completamente un espacio', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Estudiar/trabajar 1 hora concentrado', 'Dedicar tiempo focalizado al trabajo o estudio', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Llamar a un familiar o amigo', 'Conectar socialmente con alguien importante', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Organizar un espacio de trabajo', 'Ordenar y optimizar tu área de trabajo', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Meditar 15 minutos', 'Practicar mindfulness o meditación', 'MEDIA', 25, true, true, CURRENT_TIMESTAMP);

-- Tareas Difíciles (50 XP)
INSERT INTO tareas (tipo_tarea, nombre, descripcion, dificultad, xp_base, repetible, activa, fecha_creacion) VALUES
('NORMAL', 'Completar un proyecto importante del trabajo/estudio', 'Finalizar una tarea significativa pendiente', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Hacer ejercicio intenso por 1 hora', 'Entrenamiento de alta intensidad', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Limpiar toda la casa', 'Limpieza profunda de todo el hogar', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Aprender algo nuevo por 2+ horas', 'Dedicar tiempo extenso a adquirir nuevos conocimientos', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Completar una tarea que has estado posponiendo', 'Abordar esa tarea que has evitado hacer', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP),
('NORMAL', 'Hacer una actividad social fuera de tu zona de confort', 'Desafiarte socialmente con algo nuevo', 'DIFICIL', 50, true, true, CURRENT_TIMESTAMP);

-- Crear usuario de prueba
INSERT INTO usuarios (nombre, nivel, experiencia, streak_actual, fecha_creacion, fecha_ultimo_login) VALUES
('UsuarioPrueba', 1, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Crear evento inicial (10% extra XP)
INSERT INTO eventos (nombre, descripcion, multiplicador, fecha_inicio, fecha_fin, activo, fecha_creacion) VALUES
('Evento de Lanzamiento', 'Evento especial de inauguración con 10% de XP extra', 1.1, 
 DATEADD('DAY', -1, CURRENT_TIMESTAMP), DATEADD('DAY', 30, CURRENT_TIMESTAMP), true, CURRENT_TIMESTAMP);

-- Crear una tarea especial de ejemplo
INSERT INTO tareas (tipo_tarea, nombre, descripcion, dificultad, xp_base, repetible, activa, fecha_creacion, fecha_limite, una_vez_por_usuario) VALUES
('ESPECIAL', 'Completar configuración inicial', 'Tarea especial de bienvenida al sistema', 'ESPECIAL', 100, false, true, CURRENT_TIMESTAMP, 
 DATEADD('DAY', 7, CURRENT_TIMESTAMP), true);