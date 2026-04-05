# Verificación de Base de Datos SQLite

## Ubicación de la Base de Datos

La base de datos SQLite se crea automáticamente en:
```
C:\Users\[TU_USUARIO]\tareas.db
```

**Nota**: El directorio `${user.home}` se resuelve al directorio del usuario actual.

## Cómo Verificar la Base de Datos

### Opción 1: Usando DB Browser for SQLite
1. **Descargar**: https://sqlitebrowser.org/
2. **Instalar** y abrir
3. **Abrir archivo**: `C:\Users\[TU_USUARIO]\tareas.db`
4. **Ver tabla**: `tareas`

### Opción 2: Usando SQLite desde línea de comandos
```bash
# Navegar al directorio del usuario
cd C:\Users\[TU_USUARIO]

# Verificar que existe el archivo
dir tareas.db

# Abrir SQLite
sqlite3 tareas.db

# Ver tablas
.tables

# Ver estructura de tabla
.schema tareas

# Ver datos
SELECT * FROM tareas;

# Salir
.exit
```

### Opción 3: Desde IntelliJ IDEA
1. **View** → **Tool Windows** → **Database**
2. **+** → **Data Source** → **SQLite**
3. **File**: `C:\Users\[TU_USUARIO]\tareas.db`
4. **Test Connection** y **Apply**

## Verificación de Logs

Cuando ejecutes la aplicación, deberías ver en los logs:

```
Hibernate: create table tareas (
    id integer not null,
    descripcion varchar(255),
    estado varchar(255),
    titulo varchar(255),
    primary key (id)
)

Hibernate: insert into tareas (descripcion, estado, titulo) values (?, ?, ?)
```

## Problemas Comunes

### 1. Base de datos no se crea
**Síntomas**: No aparece `tareas.db`
**Causa**: Problemas de permisos o configuración
**Solución**: Verificar logs de Hibernate

### 2. Datos no se guardan
**Síntomas**: Tabla existe pero sin datos
**Causa**: Transacción no se completa
**Solución**: Verificar que no hay excepciones en logs

### 3. Error de conexión
**Causa**: Ruta incorrecta o permisos
**Solución**: Cambiar la URL en `hibernate.cfg.xml`

## Configuración Actual

```xml
<property name="hibernate.connection.url">jdbc:sqlite:${user.home}/tareas.db</property>
<property name="hibernate.hbm2ddl.auto">update</property>
<property name="hibernate.show_sql">true</property>
```

## Testing Manual

Para probar que funciona:

1. **Crear tarea**: Llenar formulario y guardar
2. **Verificar logs**: Debería aparecer SQL INSERT
3. **Verificar BD**: Abrir `tareas.db` y ver datos

---

**Ubicación esperada**: `C:\Users\[TU_USUARIO]\tareas.db`
