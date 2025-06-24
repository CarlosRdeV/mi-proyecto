# Comando: Actualizar Documentaci�n del Proyecto

## Prop�sito
Instrucciones sistem�ticas para mantener actualizada toda la documentaci�n del proyecto Task Manager Gamificado, asegurando consistencia y sincronizaci�n entre archivos.

## Sistema de Versionado
- **Versi�n actual del proyecto**: v1.3.0
- **Formato**: vX.Y.Z (semantic versioning)
- **Ubicaci�n del n�mero de versi�n**: Primera l�nea de cada archivo de documentaci�n
- **Formato de l�nea de versi�n**: `# Documentaci�n Task Manager Gamificado - Versi�n v1.3.0`

## Archivos de Documentaci�n a Actualizar

### =� Archivos Principales (OBLIGATORIOS)
1. **CLAUDE.md** - Instrucciones principales del proyecto
2. **README.md** - Descripci�n general y quick start
3. **DEPLOYMENT.md** - Gu�a de despliegue y configuraci�n
4. **DTOS.md** - Documentaci�n de DTOs y mappers
5. **FRONTEND_SPECS.md** - Especificaciones del frontend

### =� Archivos de Comandos Claude
6. **.claude/commands/check_vulnerabilities.md** - Comando de seguridad
7. **.claude/commands/update_doc.md** - Este archivo (auto-referencial)

## Procedimiento de Actualizaci�n

### PASO 1: Verificar Versi�n Actual
```bash
# Revisar la versi�n actual en CLAUDE.md
head -1 CLAUDE.md | grep -o "v[0-9]\+\.[0-9]\+\.[0-9]\+"
```

### PASO 2: Determinar Nueva Versi�n
- **Cambios menores/correcciones**: Incrementar Z (v1.3.0 � v1.2.1)
- **Nuevas funcionalidades**: Incrementar Y (v1.3.0 � v1.3.0)  
- **Cambios arquitect�nicos**: Incrementar X (v1.3.0 � v2.0.0)

### PASO 3: Actualizar Cada Archivo

#### Para CLAUDE.md:
```
1. Actualizar l�nea de versi�n al inicio
2. Revisar secci�n "Estado Actual del Desarrollo"
3. Actualizar comandos de desarrollo si hay cambios
4. Verificar endpoints API actuales
5. Actualizar conteo de tests si cambi�
```

#### Para README.md:
```
1. Actualizar l�nea de versi�n
2. Sincronizar descripci�n con CLAUDE.md
3. Actualizar quick start commands
4. Revisar badges de estado del proyecto
```

#### Para DEPLOYMENT.md:
```
1. Actualizar l�nea de versi�n
2. Revisar configuraciones de ambiente
3. Actualizar comandos Docker si cambi�
4. Verificar variables de entorno
```

#### Para DTOS.md:
```
1. Actualizar l�nea de versi�n
2. Revisar lista de DTOs implementados
3. Actualizar diagramas de mapeo
4. Sincronizar con cambios en entidades
```

#### Para FRONTEND_SPECS.md:
```
1. Actualizar l�nea de versi�n
2. Mantener sincron�a con versi�n backend
3. Actualizar endpoints disponibles
4. Revisar tipos TypeScript sugeridos
```

#### Para archivos .claude/commands/*.md:
```
1. Actualizar l�nea de versi�n
2. Revisar compatibilidad con versi�n actual
3. Actualizar comandos si hay cambios en scripts
```

### PASO 4: Verificaci�n de Consistencia

#### Comando de Verificaci�n Autom�tica:
```bash
# Verificar que todos los archivos tengan la misma versi�n
echo "Verificando consistencia de versiones..."
find . -name "*.md" -exec grep -l "Versi�n v" {} \; | while read file; do
    version=$(head -5 "$file" | grep -o "v[0-9]\+\.[0-9]\+\.[0-9]\+" | head -1)
    echo "$file: $version"
done
```

#### Lista de Verificaci�n Manual:
- [ ] Todos los archivos muestran la misma versi�n
- [ ] Fechas de �ltima actualizaci�n son recientes
- [ ] Enlaces internos funcionan correctamente
- [ ] Informaci�n t�cnica est� sincronizada
- [ ] Comandos de ejemplo son v�lidos

## Plantilla de L�nea de Versi�n

```markdown
# Documentaci�n Task Manager Gamificado - Versi�n v1.3.0
*�ltima actualizaci�n: 2024-12-XX*
```

## Notas Importantes

### � Reglas Cr�ticas:
1. **NUNCA** actualizar solo un archivo - siempre en conjunto
2. **SIEMPRE** usar la misma versi�n en todos los archivos
3. **VERIFICAR** que los comandos de ejemplo funcionen
4. **MANTENER** sincron�a entre backend y frontend specs

### = Flujo de Trabajo:
1. Detectar cambios en c�digo/arquitectura
2. Determinar tipo de cambio (major/minor/patch)
3. Incrementar versi�n siguiendo semantic versioning
4. Actualizar TODOS los archivos de documentaci�n
5. Ejecutar verificaci�n de consistencia
6. Commit todos los cambios de documentaci�n juntos

### =� Mensaje de Commit Sugerido:
```
docs: actualizar documentaci�n a versi�n vX.Y.Z

- Sincronizar versiones en todos los archivos .md
- Actualizar estado del desarrollo
- Revisar comandos y configuraciones
- Mantener consistencia entre backend/frontend specs
```

## Comando R�pido para Claude

**Prompt sugerido para usar con Claude:**
```
"Actualiza toda la documentaci�n del proyecto a la versi�n v[NUEVA_VERSION]. 
Aseg�rate de:
1. Cambiar la l�nea de versi�n en TODOS los archivos .md
2. Actualizar fechas de �ltima modificaci�n
3. Revisar consistencia de informaci�n t�cnica
4. Verificar que comandos de ejemplo funcionen
5. Confirmar que versiones backend/frontend est�n sincronizadas"
```

---
*Este comando debe ejecutarse cada vez que se realicen cambios significativos al proyecto para mantener la documentaci�n actualizada y consistente.*