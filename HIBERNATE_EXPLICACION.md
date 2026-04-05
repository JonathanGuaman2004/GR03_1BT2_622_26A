# ¿Qué es Hibernate? - Guía Completa

## Introducción

**Hibernate** es un framework **ORM (Object-Relational Mapping)** que permite mapear objetos Java directamente a tablas de una base de datos relacional.

Sin Hibernate necesitarías escribir SQL manualmente:
```java
// Sin Hibernate (SQL directo)
String query = "SELECT * FROM tareas WHERE id = ?";
PreparedStatement stmt = connection.prepareStatement(query);
stmt.setInt(1, 1);
// ... más código ...
```

Con Hibernate simplemente haces:
```java
// Con Hibernate (ORM)
Tarea tarea = session.get(Tarea.class, 1);
```

## Ventajas de Hibernate

| Ventaja | Descripción |
|---------|------------|
| **Abstracción** | No escribes SQL, trabajas con objetos Java |
| **Independencia de BD** | Cambiar de BD solo requiere cambiar el dialecto |
| **Generación Automática** | Crea tablas automáticamente |
| **Relaciones** | Soporta relaciones entre entidades (1:1, 1:N, N:N) |
| **Lazy Loading** | Carga datos bajo demanda |
| **Caché** | Rendimiento mejorado con caché |

## Componentes Principales de Hibernate

### 1. **SessionFactory**
Es una "fábrica" que crea sesiones (conexiones) con la BD.

```java
SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
```

- Se crea una única vez al iniciar la aplicación
- Thread-safe
- Muy costosa de crear, por eso se hace una sola

**En tu proyecto**: Se crea de forma perezosa (lazy) en `getSessionFactory()`

### 2. **Session**
Representa una conexión con la BD.

```java
Session session = sessionFactory.openSession();
```

- Corta vida (se cierra después de la operación)
- No es thread-safe
- Se usa para CRUD (Create, Read, Update, Delete)

**En tu proyecto**: Se usa en try-with-resources:
```java
try (Session session = getSessionFactory().openSession()) {
    // operaciones...
}
```

### 3. **Configuration**
Lee la configuración de Hibernate del archivo `hibernate.cfg.xml`.

```java
Configuration config = new Configuration().configure();
```

Contiene:
- Dialecto SQL (SQLite en tu caso)
- Conexión a BD
- Clases mapeadas
- Propiedades de comportamiento

### 4. **Transaction**
Maneja transacciones en la BD.

```java
session.beginTransaction();
try {
    // operaciones...
    session.getTransaction().commit();
} catch (Exception e) {
    session.getTransaction().rollback();
}
```

## Flujo en tu Aplicación

### Flujo de Guardado

```
1. Usuario submite formulario
                ↓
2. TareaServlet.guardarTarea() recibe datos
                ↓
3. Crea objeto Tarea
                ↓
4. Llama tareaDAO.guardar(tarea)
                ↓
5. TareaDAO obtiene Session de SessionFactory
                ↓
6. Inicia transacción
                ↓
7. Ejecuta session.persist(tarea)
                ↓
8. Hibernate convierte el objeto Java a SQL INSERT
                ↓
9. Commit de la transacción
                ↓
10. Datos guardados en tareas.db (SQLite)
```

### Flujo de Lectura

```
1. Usuario accede a /tareas?action=listar
                ↓
2. TareaServlet.listarTareas() se ejecuta
                ↓
3. Llama tareaDAO.listar()
                ↓
4. TareaDAO obtiene Session de SessionFactory
                ↓
5. Ejecuta session.createQuery("from Tarea")
                ↓
6. Hibernate convierte a SQL SELECT * FROM tareas
                ↓
7. Lee datos de tareas.db
                ↓
8. Convierte filas de BD a objetos Tarea
                ↓
9. Devuelve List<Tarea>
                ↓
10. TareaServlet pasa lista a JSP para mostrar
```

## Configuración en tu Proyecto

### `hibernate.cfg.xml`

```xml
<hibernate-configuration>
    <session-factory>
        <!-- 1. Dialecto SQL -->
        <property name="hibernate.dialect">org.hibernate.dialect.SQLiteDialect</property>
        
        <!-- 2. Driver JDBC -->
        <property name="hibernate.connection.driver_class">org.sqlite.JDBC</property>
        
        <!-- 3. URL de conexión -->
        <property name="hibernate.connection.url">jdbc:sqlite:tareas.db</property>
        
        <!-- 4. Auto-generación de tablas -->
        <property name="hibernate.hbm2ddl.auto">update</property>
        
        <!-- 5. Mostrar SQL en consola (debugging) -->
        <property name="hibernate.show_sql">true</property>
        
        <!-- 6. Clases mapeadas -->
        <mapping class="com.webapp.gr03_1bt2_622_26a.Tarea"/>
    </session-factory>
</hibernate-configuration>
```

### Propiedad: `hibernate.hbm2ddl.auto`

| Valor | Comportamiento |
|-------|----------------|
| `create` | Destruye tabla y la recrea (pierde datos) |
| `create-drop` | Crea al iniciar, destruye al cerrar |
| `update` | **Recomendado para desarrollo** - Actualiza schema sin perder datos |
| `validate` | Valida que el schema coincida |
| `none` | No hace nada |

**Tu proyecto usa `update`**: Las columnas se agregan según sea necesario sin perder datos.

## Mapeo de Entidades

### Clase Tarea (Entidad)

```java
@Entity                              // Esta clase es una entidad
@Table(name = "tareas")              // Se mapea a tabla "tareas"
public class Tarea {
    @Id                              // Clave primaria
    @GeneratedValue                  // Auto-generada
    (strategy = GenerationType.IDENTITY)
    private int id;
    
    private String titulo;           // Se mapea automáticamente a columna "titulo"
    private String descripcion;      // Se mapea a columna "descripcion"
    private String estado;           // Se mapea a columna "estado"
    
    // Getters y Setters...
}
```

**Resultado en BD**:
```
Tabla: tareas
Columnas:
- id (INTEGER PRIMARY KEY AUTOINCREMENT)
- titulo (VARCHAR)
- descripcion (TEXT)
- estado (VARCHAR)
```

## Operaciones CRUD en TareaDAO

### CREATE (Guardar)
```java
public void guardar(Tarea tarea) {
    try (Session session = getSessionFactory().openSession()) {
        session.beginTransaction();
        try {
            session.persist(tarea);  // Marca para insertar
            session.getTransaction().commit();  // Ejecuta INSERT
        } catch (Exception e) {
            session.getTransaction().rollback();  // Deshace cambios
            throw e;
        }
    }
}
```

### READ (Obtener)
```java
public Tarea obtenerPorId(int id) {
    try (Session session = getSessionFactory().openSession()) {
        return session.get(Tarea.class, id);  // Ejecuta SELECT
    }
}
```

### READ (Listar todos)
```java
public List<Tarea> listar() {
    try (Session session = getSessionFactory().openSession()) {
        return session.createQuery("from Tarea", Tarea.class).list();
        // Ejecuta: SELECT * FROM tareas
    }
}
```

### UPDATE (Actualizar)
```java
public void actualizar(Tarea tarea) {
    try (Session session = getSessionFactory().openSession()) {
        session.beginTransaction();
        try {
            session.merge(tarea);  // Marca para actualizar
            session.getTransaction().commit();  // Ejecuta UPDATE
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
```

### DELETE (Eliminar)
```java
public void eliminar(int id) {
    try (Session session = getSessionFactory().openSession()) {
        session.beginTransaction();
        try {
            Tarea tarea = session.get(Tarea.class, id);
            if (tarea != null) {
                session.remove(tarea);  // Marca para eliminar
            }
            session.getTransaction().commit();  // Ejecuta DELETE
        } catch (Exception e) {
            session.getTransaction().rollback();
            throw e;
        }
    }
}
```

## SQLite en Hibernate

SQLite es una BD embebida (archivo local):

```java
// Configuración
<property name="hibernate.connection.url">jdbc:sqlite:tareas.db</property>
```

**Ventajas**:
- ✅ Sin servidor externo
- ✅ Archivo único (fácil de mover/respaldar)
- ✅ Perfecto para desarrollo/testing

**Desventajas**:
- ❌ No ideal para producción con mucha concurrencia
- ❌ Bloqueos a nivel de archivo

**Archivo de BD**: `tareas.db` (se crea en el directorio de trabajo)

## Ciclo de Vida de una Sesión

```
1. TRANSIENT
   ↓ (al cargar)
2. PERSISTENT (en sesión)
   ↓ (sync con BD)
3. DETACHED (cierra sesión)
```

**Transient**: Objeto Java no vinculado a BD
```java
Tarea tarea = new Tarea("Mi tarea", "...", "pendiente");
// Transient - no existe en BD
```

**Persistent**: Objeto en una sesión activa
```java
try (Session session = sessionFactory.openSession()) {
    session.persist(tarea);  // Ahora es PERSISTENT
    // Cambios en tarea se sincronizan con BD automáticamente
}
```

**Detached**: Sesión cerrada
```java
// Después de cerrar sesión
// tarea sigue existiendo en memoria pero está DETACHED
// Cambios NO se sincronizan automáticamente
```

## Inicialización en tu Proyecto

### Antes (Problema):
```java
static {
    try {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    } catch (Exception e) {
        throw new RuntimeException(...);  // Falla al cargar clase
    }
}
```
❌ Si hay error, la clase no se carga, servlet falla

### Después (Solución):
```java
private static synchronized SessionFactory getSessionFactory() {
    if (sessionFactory == null && !initialized) {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            initialized = true;
        } catch (Exception e) {
            initialized = true;
            throw new RuntimeException(...);  // Error reportado al usar DAO
        }
    }
    return sessionFactory;
}
```
✅ Si hay error, se reporta cuando usas DAO (más tarde), permitiendo cargar servlet

## Ventajas de la Solución Implementada

1. **Lazy Initialization**: Hibernate se carga solo cuando se necesita
2. **Thread-Safe**: `synchronized` evita race conditions
3. **Mejor Logging**: Los errores se detallan en la consola
4. **Manejo Robusto**: Try-catch en cada operación

## Resumen

| Concepto | Explicación |
|----------|------------|
| **ORM** | Mapea objetos Java a tablas BD |
| **SessionFactory** | Fábrica de sesiones (conexiones) |
| **Session** | Una conexión con la BD |
| **Dialecto** | Especifica el tipo de BD (SQLite, MySQL, etc) |
| **Entidad** | Clase Java mapeada a tabla |
| **Transacción** | Operación atómica (todo o nada) |
| **Lazy Loading** | Carga datos bajo demanda |

---

**Conclusión**: Hibernate simplifica el acceso a datos permitiéndote trabajar con objetos Java en lugar de SQL directo.

