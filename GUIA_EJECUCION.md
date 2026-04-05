# Guía de Ejecución - Sistema Web de Gestión de Tareas

## ✅ Estado del Proyecto
- **Compilación**: ✅ Exitosa
- **Empaquetamiento**: ✅ Exitoso (WAR generado)
- **Errores**: ✅ Solucionados

## Cambios Principales Realizados

### 1. **Inicialización de Hibernate - Lazy Loading**
   - Se cambió de inicialización estática a inicialización perezosa
   - Método `getSessionFactory()` sincronizado para thread-safety
   - Los errores ahora se reportan con detalle al momento de usar DAO

### 2. **Inicialización del TareaDAO en Servlet**
   - Se movió de declaración de variable a método `init()`
   - Proporciona mejor control del ciclo de vida del servlet

### 3. **Manejo Robusto de Excepciones**
   - Todos los métodos DAO tienen try-catch
   - Todos los métodos del servlet tienen try-catch
   - Se devuelven valores seguros en caso de error

### 4. **Compatibilidad Java 8**
   - Se reemplazó `List.of()` (Java 9+) con `new ArrayList<>()`

## Cómo Ejecutar la Aplicación

### Opción 1: Con Maven y Tomcat
```powershell
# 1. Compilar y empaquetar
cd "C:\Users\jonat\OneDrive\Documents\IdeaProjects\GR03_1BT2_622_26A"
mvn clean package

# 2. El archivo WAR se generará en: target/GR03_1BT2_622_26A-1.0-SNAPSHOT.war

# 3. Copiar el archivo WAR a CATALINA_HOME/webapps
# Ejemplo: C:\apache-tomcat-10.1.x\webapps\

# 4. Iniciar Tomcat
# El archivo se desplegará automáticamente

# 5. Acceder a: http://localhost:8080/GR03_1BT2_622_26A-1.0-SNAPSHOT/tareas
```

### Opción 2: Desde IntelliJ IDEA
1. Click derecho en el proyecto
2. Run → Run 'GR03_1BT2_622_26A' (si está configurado)
3. O Configure Run Configuration para usar Tomcat

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/webapp/gr03_1bt2_622_26a/
│   │   ├── Tarea.java              (Entidad - Modelo)
│   │   ├── TareaDAO.java           (DAO - Acceso a Datos)
│   │   ├── TareaServlet.java       (Servlet - Controlador)
│   │   └── HelloServlet.java       (Servlet de prueba)
│   ├── webapp/
│   │   ├── listaTareas.jsp         (Vista - Listar)
│   │   ├── crearTarea.jsp          (Vista - Crear)
│   │   ├── editarTarea.jsp         (Vista - Editar)
│   │   ├── index.jsp               (Inicio)
│   │   └── WEB-INF/
│   │       └── web.xml             (Configuración web)
│   └── resources/
│       ├── hibernate.cfg.xml       (Configuración Hibernate)
│       └── META-INF/beans.xml      (Configuración CDI)
```

## Endpoints Disponibles

| Acción | URL | Método | Descripción |
|--------|-----|--------|-------------|
| Listar | `/tareas?action=listar` | GET | Muestra todas las tareas |
| Crear (Formulario) | `/tareas?action=crear` | GET | Muestra formulario crear |
| Crear (Guardar) | `/tareas` | POST | `action=guardar` |
| Editar (Formulario) | `/tareas?action=editar&id=1` | GET | Muestra formulario editar |
| Actualizar | `/tareas` | POST | `action=actualizar` |
| Eliminar | `/tareas?action=eliminar&id=1` | GET | Elimina una tarea |

## Base de Datos - SQLite

**Archivo**: `tareas.db` (se crea automáticamente en el directorio de trabajo)

**Tabla**: `tareas`

```sql
CREATE TABLE tareas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo VARCHAR(255) NOT NULL,
    descripcion TEXT,
    estado VARCHAR(50)
);
```

La tabla se crea automáticamente por Hibernate mediante la configuración:
```xml
<property name="hibernate.hbm2ddl.auto">update</property>
```

## Solución de Problemas

### Error: "package org.hibernate does not exist"
**Solución**: Asegúrate de que Maven haya descargado las dependencias:
```powershell
mvn clean install
```

### Error: "tareas.db not found"
**Solución**: Hibernate lo crea automáticamente en la primera ejecución. Verifica que la aplicación tenga permisos de escritura.

### Error: "Error al cargar sesión de Hibernate"
**Solución**: 
1. Verifica el archivo `hibernate.cfg.xml`
2. Asegúrate de que SQLite está en el classpath
3. Revisa los logs de error en la consola

### No aparecen las tareas guardadas
**Solución**:
1. Verifica el archivo `tareas.db` existe
2. Comprueba que el estado de transacción es correcto
3. Revisa los logs de Hibernate (`hibernate.show_sql=true`)

## Comando de Compilación Rápido

```powershell
cd "C:\Users\jonat\OneDrive\Documents\IdeaProjects\GR03_1BT2_622_26A"
mvn clean compile
```

## Comando de Empaquetamiento Rápido

```powershell
cd "C:\Users\jonat\OneDrive\Documents\IdeaProjects\GR03_1BT2_622_26A"
mvn clean package
```

## Información de Dependencias

| Dependencia | Versión | Propósito |
|-------------|---------|----------|
| jakarta.servlet-api | 6.1.0 | API de Servlets (Jakarta) |
| hibernate-core | 6.5.2.Final | ORM - Mapeo Objeto-Relacional |
| sqlite-jdbc | 3.46.0.0 | Driver JDBC para SQLite |
| jakarta.persistence-api | 3.1.0 | API de Persistencia |
| jersey-container-servlet | 4.0.0-M2 | REST Framework |
| junit-jupiter-api | 5.13.2 | Testing Framework |

## Verificación de Compilación

Después de ejecutar `mvn clean compile`, deberías ver:

```
[INFO] BUILD SUCCESS
```

## Verificación de Empaquetamiento

Después de ejecutar `mvn clean package`, deberías ver:

```
[INFO] Building war: .../target/GR03_1BT2_622_26A-1.0-SNAPSHOT.war
[INFO] BUILD SUCCESS
```

---

**Estado Actual**: ✅ Listo para desplegar
**Última Actualización**: 2026-04-04
**Versión**: 1.0-SNAPSHOT

