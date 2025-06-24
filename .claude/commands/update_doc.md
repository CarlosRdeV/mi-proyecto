# Comando: Actualizar Documentación del Proyecto

## Propósito
Instrucciones sistemáticas para mantener actualizada toda la documentación del proyecto Task Manager Gamificado, asegurando consistencia y sincronización entre archivos.

## Sistema de Versionado
- **Versión actual del proyecto**: v1.3.0
- **Formato**: vX.Y.Z (semantic versioning)
- **Ubicación del número de versión**: Primera línea de cada archivo de documentación
- **Formato de línea de versión**: `# Documentación Task Manager Gamificado - Versión v1.3.0`

## Archivos de Documentación a Actualizar

### =Ë Archivos Principales (OBLIGATORIOS)
1. **CLAUDE.md** - Instrucciones principales del proyecto
2. **README.md** - Descripción general y quick start
3. **DEPLOYMENT.md** - Guía de despliegue y configuración
4. **DTOS.md** - Documentación de DTOs y mappers
5. **FRONTEND_SPECS.md** - Especificaciones del frontend

### =Á Archivos de Comandos Claude
6. **.claude/commands/check_vulnerabilities.md** - Comando de seguridad
7. **.claude/commands/update_doc.md** - Este archivo (auto-referencial)

## Procedimiento de Actualización

### PASO 1: Verificar Versión Actual
```bash
# Revisar la versión actual en CLAUDE.md
head -1 CLAUDE.md | grep -o "v[0-9]\+\.[0-9]\+\.[0-9]\+"
```

### PASO 2: Determinar Nueva Versión
- **Cambios menores/correcciones**: Incrementar Z (v1.3.0 ’ v1.2.1)
- **Nuevas funcionalidades**: Incrementar Y (v1.3.0 ’ v1.3.0)  
- **Cambios arquitectónicos**: Incrementar X (v1.3.0 ’ v2.0.0)

### PASO 3: Actualizar Cada Archivo

#### Para CLAUDE.md:
```
1. Actualizar línea de versión al inicio
2. Revisar sección "Estado Actual del Desarrollo"
3. Actualizar comandos de desarrollo si hay cambios
4. Verificar endpoints API actuales
5. Actualizar conteo de tests si cambió
```

#### Para README.md:
```
1. Actualizar línea de versión
2. Sincronizar descripción con CLAUDE.md
3. Actualizar quick start commands
4. Revisar badges de estado del proyecto
```

#### Para DEPLOYMENT.md:
```
1. Actualizar línea de versión
2. Revisar configuraciones de ambiente
3. Actualizar comandos Docker si cambió
4. Verificar variables de entorno
```

#### Para DTOS.md:
```
1. Actualizar línea de versión
2. Revisar lista de DTOs implementados
3. Actualizar diagramas de mapeo
4. Sincronizar con cambios en entidades
```

#### Para FRONTEND_SPECS.md:
```
1. Actualizar línea de versión
2. Mantener sincronía con versión backend
3. Actualizar endpoints disponibles
4. Revisar tipos TypeScript sugeridos
```

#### Para archivos .claude/commands/*.md:
```
1. Actualizar línea de versión
2. Revisar compatibilidad con versión actual
3. Actualizar comandos si hay cambios en scripts
```

### PASO 4: Verificación de Consistencia

#### Comando de Verificación Automática:
```bash
# Verificar que todos los archivos tengan la misma versión
echo "Verificando consistencia de versiones..."
find . -name "*.md" -exec grep -l "Versión v" {} \; | while read file; do
    version=$(head -5 "$file" | grep -o "v[0-9]\+\.[0-9]\+\.[0-9]\+" | head -1)
    echo "$file: $version"
done
```

#### Lista de Verificación Manual:
- [ ] Todos los archivos muestran la misma versión
- [ ] Fechas de última actualización son recientes
- [ ] Enlaces internos funcionan correctamente
- [ ] Información técnica está sincronizada
- [ ] Comandos de ejemplo son válidos

## Plantilla de Línea de Versión

```markdown
# Documentación Task Manager Gamificado - Versión v1.3.0
*Última actualización: 2024-12-XX*
```

## Notas Importantes

###   Reglas Críticas:
1. **NUNCA** actualizar solo un archivo - siempre en conjunto
2. **SIEMPRE** usar la misma versión en todos los archivos
3. **VERIFICAR** que los comandos de ejemplo funcionen
4. **MANTENER** sincronía entre backend y frontend specs

### = Flujo de Trabajo:
1. Detectar cambios en código/arquitectura
2. Determinar tipo de cambio (major/minor/patch)
3. Incrementar versión siguiendo semantic versioning
4. Actualizar TODOS los archivos de documentación
5. Ejecutar verificación de consistencia
6. Commit todos los cambios de documentación juntos

### =Ý Mensaje de Commit Sugerido:
```
docs: actualizar documentación a versión vX.Y.Z

- Sincronizar versiones en todos los archivos .md
- Actualizar estado del desarrollo
- Revisar comandos y configuraciones
- Mantener consistencia entre backend/frontend specs
```

## Comando Rápido para Claude

**Prompt sugerido para usar con Claude:**
```
"Actualiza toda la documentación del proyecto a la versión v[NUEVA_VERSION]. 
Asegúrate de:
1. Cambiar la línea de versión en TODOS los archivos .md
2. Actualizar fechas de última modificación
3. Revisar consistencia de información técnica
4. Verificar que comandos de ejemplo funcionen
5. Confirmar que versiones backend/frontend estén sincronizadas"
```

---
*Este comando debe ejecutarse cada vez que se realicen cambios significativos al proyecto para mantener la documentación actualizada y consistente.*