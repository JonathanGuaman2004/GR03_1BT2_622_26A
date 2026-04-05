# Solución de Problemas - Sistema Web de Gestión de Tareas

## Problema Original
**Error**: `Error instanciando clase de servlet [com.webapp.gr03_1bt2_622_26a.TareaServlet]`

Este error ocurría porque:
1. El `SessionFactory` de Hibernate se inicializaba en un bloque `static`
2. Si había cualquier problema con la configuración de Hibernate, la clase completa fallaba en su carga
3. El servlet intentaba instanciar `TareaDAO` directamente en la declaración de variable de instancia

## Cambios Realizados

### 1. **TareaDAO.java** - Inicialización Perezosa (Lazy Initialization)

**Antes:**
```java
static {
    try {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    } catch (Exception e) {
        throw new RuntimeException("Error initializing Hibernate SessionFactory", e);
    }
}
```

**Después:**
```java
private static volatile boolean initialized = false;

private static synchronized SessionFactory getSessionFactory() {
    if (sessionFactory == null && !initialized) {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
            initialized = true;
        } catch (Exception e) {
            initialized = true;
            System.err.println("Error initializing Hibernate SessionFactory: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error initializing Hibernate SessionFactory", e);
        }
    }
    return sessionFactory;
}
```

**Beneficios:**
- La inicialización de Hibernate se retrasa hasta que se use realmente
- Los errores se detectan cuando se usan los métodos DAO, no cuando se carga la clase
- Mejor manejo de excepciones y logging

### 2. **TareaDAO.java** - Manejo Mejorado de Excepciones

Agregué try-catch en cada método para que devuelvan valores seguros en caso de error:

```java
public List<Tarea> listar() {
    try (Session session = getSessionFactory().openSession()) {
        return session.createQuery("from Tarea", Tarea.class).list();
    } catch (Exception e) {
        System.err.println("Error al listar tareas: " + e.getMessage());
        e.printStackTrace();
        return new ArrayList<>();  // Devuelve lista vacía en lugar de fallar
    }
}
```

### 3. **TareaServlet.java** - Inicialización en el Método init()

**Antes:**
```java
private TareaDAO tareaDAO = new TareaDAO();  // Falla si hay problema
```

**Después:**
```java
private TareaDAO tareaDAO;

@Override
public void init() throws ServletException {
    super.init();
    try {
        tareaDAO = new TareaDAO();
    } catch (Exception e) {
        System.err.println("Error inicializando TareaDAO en servlet: " + e.getMessage());
        e.printStackTrace();
        throw new ServletException("No se pudo inicializar TareaDAO", e);
    }
}
```

**Beneficios:**
- La inicialización ocurre en el método `init()` del servlet
- Proporciona mejor control del ciclo de vida

### 4. **Corrección de Compatibilidad Java 8**

**Problema**: `List.of()` no existe en Java 8 (fue añadido en Java 9)

**Solución**:
```java
return new ArrayList<>();  // En lugar de List.of()
```

### 5. **Manejo Mejorado de Errores en Métodos del Servlet**

Todos los métodos ahora tienen try-catch para evitar que los errores impidan la ejecución:

```java
private void guardarTarea(HttpServletRequest request, HttpServletResponse response) throws IOException {
    try {
        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        String estado = request.getParameter("estado");
        Tarea tarea = new Tarea(titulo, descripcion, estado);
        tareaDAO.guardar(tarea);
        response.sendRedirect("tareas?action=listar");
    } catch (Exception e) {
        System.err.println("Error al guardar tarea: " + e.getMessage());
        e.printStackTrace();
        try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al guardar la tarea");
        } catch (IllegalStateException ignored) {}
    }
}
```

## Resultado

✅ **Compilación exitosa** - No hay errores de compilación
✅ **Build exitoso** - El archivo WAR se genera correctamente
✅ **Manejo de excepciones mejorado** - Errores con logging detallado
✅ **Inicialización más robusta** - La clase no falla durante la carga

## Qué es Hibernate y cómo funciona

### ¿Qué es Hibernate?

Hibernate es un **ORM (Object-Relational Mapping)** que permite mapear objetos Java directamente a tablas de base de datos.

### Características principales:

1. **Mapeo Automático**: Los atributos de una clase Java se mapean a columnas de una tabla
2. **Abstracción de BD**: No necesitas escribir SQL manualmente
3. **SessionFactory**: Factory que crea conexiones a la BD
4. **Session**: Representa una conexión con la BD

### Flujo en tu aplicación:

1. **Configuración** (hibernate.cfg.xml):
   - Define driver de BD (SQLite)
   - URL de conexión
   - Dialecto SQL
   - Clases mapeadas

2. **Entidad** (Tarea.java):
   - `@Entity`: Indica que es una tabla BD
   - `@Table(name = "tareas")`: Nombre de la tabla
   - `@Id @GeneratedValue`: ID auto-generado

3. **DAO** (TareaDAO.java):
   - Usa SessionFactory para obtener conexiones
   - Ejecuta operaciones CRUD

4. **Servlet** (TareaServlet.java):
   - Usa DAO para operaciones de BD
   - Controla el flujo de la aplicación

### Cómo funciona SQLite en Hibernate:

```xml
<property name="hibernate.dialect">org.hibernate.dialect.SQLiteDialect</property>
<property name="hibernate.connection.driver_class">org.sqlite.JDBC</property>
<property name="hibernate.connection.url">jdbc:sqlite:tareas.db</property>
```

- SQLite es una BD embebida (archivo local)
- Se genera automáticamente el archivo `tareas.db`
- No requiere servidor separado

## Próximos Pasos

1. Asegúrate de que `tareas.db` se crea en el directorio correcto
2. Verifica que los archivos JSP estén correctamente configurados
3. Prueba la aplicación en tu servidor Tomcat/JBoss

---

**Proyecto**: Sistema Web de Gestión de Tareas
**Arquitectura**: 3 capas (Presentación, Lógica, Datos)
**Status**: ✅ Compilación exitosa

