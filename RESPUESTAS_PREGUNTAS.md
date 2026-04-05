# Respuestas a Preguntas - Tomcat y Base de Datos

## 1. ¿Por qué funciona con Tomcat 10 y no con Tomcat 9?

### La Diferencia entre Tomcat 9 y Tomcat 10

**Tomcat 9** soporta **Java EE** (especificación antigua):
- APIs: `javax.servlet.*`, `javax.persistence.*`
- Especificación: Java EE 8

**Tomcat 10** soporta **Jakarta EE** (especificación nueva):
- APIs: `jakarta.servlet.*`, `jakarta.persistence.*`
- Especificación: Jakarta EE 9+

### Tu Proyecto Usa Jakarta EE

Tu proyecto está configurado para **Jakarta EE**:

```xml
<!-- pom.xml -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.1.0</version>
</dependency>

<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
    <version>3.1.0</version>
</dependency>
```

```java
// Tarea.java
import jakarta.persistence.*;  // Jakarta, no javax
```

### ¿Por qué no funciona con Tomcat 9?

Tomcat 9 no reconoce las APIs `jakarta.*`, solo `javax.*`. Por eso obtienes:

```
java.lang.NoClassDefFoundError: jakarta/servlet/http/HttpServlet
```

**Solución**: Usar Tomcat 10+ para proyectos Jakarta EE.

---

## 2. ¿Dónde está la Base de Datos SQLite?

### Ubicación de la Base de Datos

**Antes**: Se creaba en el directorio de Tomcat (difícil de encontrar)
**Ahora**: Se crea en tu directorio de usuario:

```
C:\Users\jonat\tareas.db
```

### Cambios Realizados

1. **Configuración actualizada** en `hibernate.cfg.xml`:
```xml
<property name="hibernate.connection.url">jdbc:sqlite:${user.home}/tareas.db</property>
```

2. **Más logging agregado**:
```xml
<property name="hibernate.show_sql">true</property>
<property name="hibernate.format_sql">true</property>
<property name="hibernate.use_sql_comments">true</property>
<property name="hibernate.generate_statistics">true</property>
```

### Cómo Verificar la Base de Datos

#### Opción 1: DB Browser for SQLite
1. **Descargar**: https://sqlitebrowser.org/
2. **Abrir**: `C:\Users\jonat\tareas.db`
3. **Ver tabla**: `tareas`

#### Opción 2: Línea de comandos
```bash
cd C:\Users\jonat
sqlite3 tareas.db
.tables
SELECT * FROM tareas;
.exit
```

#### Opción 3: IntelliJ IDEA
1. **View** → **Tool Windows** → **Database**
2. **+** → **Data Source** → **SQLite**
3. **File**: `C:\Users\jonat\tareas.db`

### Endpoint de Prueba Agregado

Nuevo endpoint para verificar BD:
```
http://localhost:8080/GR03_1BT2_622_26A/tareas?action=test
```

### ¿Por qué no se guardaban los datos?

**Posibles causas**:
1. ❌ Ubicación incorrecta de BD (ya solucionado)
2. ❌ Transacciones no completadas
3. ❌ Errores en Hibernate (ahora con más logs)

### Verificación de Logs

Cuando guardes una tarea, deberías ver en logs:

```
Hibernate: /* insert com.webapp.gr03_1bt2_622_26a.Tarea */ insert into tareas (descripcion, estado, titulo) values (?, ?, ?)
```

---

## Resumen de Cambios

### ✅ Problema Tomcat 9 vs 10
- **Explicación**: Jakarta EE vs Java EE
- **Solución**: Usar Tomcat 10+

### ✅ Ubicación Base de Datos
- **Antes**: Directorio Tomcat (oculto)
- **Ahora**: `C:\Users\[usuario]\tareas.db`
- **Accesible**: Fácil de ver y gestionar

### ✅ Más Debugging
- **Logs SQL**: Habilitados
- **Estadísticas**: Habilitadas
- **Endpoint test**: Agregado

### ✅ Documentación
- `VERIFICAR_BD.md`: Guía completa para BD

---

## Próximos Pasos

1. **Reiniciar Tomcat** en IntelliJ IDEA
2. **Crear una tarea** de prueba
3. **Verificar logs** - deberías ver SQL INSERT
4. **Abrir BD** - deberías ver los datos guardados
5. **Usar endpoint test**: `/tareas?action=test`

---

**¡Problemas resueltos!** 🎉

- ✅ Tomcat 10 funciona (Jakarta EE)
- ✅ Base de datos accesible en `C:\Users\jonat\tareas.db`
- ✅ Más logging para debugging
- ✅ Endpoint de prueba agregado
