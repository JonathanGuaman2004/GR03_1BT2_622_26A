# Configuración de Tomcat en IntelliJ IDEA

## Problema Resuelto
El error `java.lang.NoClassDefFoundError: jakarta/servlet/http/HttpServlet` ocurría porque:

1. **Dependencias con scope `provided`**: Las APIs de servlet no se incluyen en el WAR
2. **IntelliJ IDEA**: Necesita las dependencias en tiempo de ejecución para ejecutar desde IDE

## Solución Implementada

### 1. Perfil Maven para Desarrollo
Se agregó un perfil `development` en `pom.xml` que cambia el scope de `jakarta.servlet-api` de `provided` a `compile`.

```xml
<profiles>
    <profile>
        <id>development</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <dependencies>
            <dependency>
                <groupId>jakarta.servlet</groupId>
                <artifactId>jakarta.servlet-api</artifactId>
                <version>6.1.0</version>
                <scope>compile</scope>
            </dependency>
        </dependencies>
    </profile>
</profiles>
```

## Cómo Configurar Tomcat en IntelliJ IDEA

### Paso 1: Instalar Tomcat
1. Descargar Apache Tomcat 10.x desde: https://tomcat.apache.org/download-10.cgi
2. Extraer en una carpeta (ej: `C:\apache-tomcat-10.x`)

### Paso 2: Configurar en IntelliJ IDEA
1. **Abrir IntelliJ IDEA**
2. **Abrir el proyecto**: `GR03_1BT2_622_26A`
3. **Ir a**: `Run` → `Edit Configurations`
4. **Click en `+`** → `Tomcat Server` → `Local`
5. **Configurar**:
   - **Name**: `Tomcat 10`
   - **Application server**: Click `Configure...` y seleccionar la carpeta de Tomcat
   - **JRE**: Seleccionar Java 8 o superior
6. **Pestaña "Deployment"**:
   - Click `+` → `Artifact`
   - Seleccionar: `GR03_1BT2_622_26A:war exploded`
   - **Application context**: `/GR03_1BT2_622_26A`

### Paso 3: Ejecutar
1. **Seleccionar configuración**: `Tomcat 10` en la barra superior
2. **Click en Run** (botón verde de play)
3. **Esperar** a que Tomcat inicie
4. **Acceder**: `http://localhost:8080/GR03_1BT2_622_26A/tareas`

## Comandos Maven

### Para desarrollo (desde IntelliJ)
```bash
mvn clean compile -Pdevelopment
```

### Para producción (despliegue)
```bash
mvn clean package
```

## Verificación

Después de configurar correctamente, deberías ver en la consola:
```
[INFO] BUILD SUCCESS
```

Y en el navegador:
- ✅ Página de lista de tareas
- ✅ Funcionalidad CRUD completa

## Troubleshooting

### Error: "Tomcat no puede iniciar"
**Solución**: Verificar que el puerto 8080 no esté ocupado
```bash
netstat -ano | findstr :8080
```

### Error: "Artifact no encontrado"
**Solución**: Ejecutar `mvn clean compile` primero

### Error: "Context path ya existe"
**Solución**: Cambiar el application context en la configuración

---

**Estado**: ✅ Configurado para IntelliJ IDEA
**Perfil**: development activado por defecto
