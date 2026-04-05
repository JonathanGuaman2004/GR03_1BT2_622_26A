<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Crear Tarea</title>
</head>
<body>
    <h1>Crear Nueva Tarea</h1>
    <form action="tareas" method="post">
        <input type="hidden" name="action" value="guardar">
        <label for="titulo">Título:</label>
        <input type="text" id="titulo" name="titulo" required><br><br>
        <label for="descripcion">Descripción:</label>
        <textarea id="descripcion" name="descripcion" required></textarea><br><br>
        <label for="estado">Estado:</label>
        <select id="estado" name="estado">
            <option value="Pendiente">Pendiente</option>
            <option value="En Progreso">En Progreso</option>
            <option value="Completada">Completada</option>
        </select><br><br>
        <input type="submit" value="Guardar">
    </form>
    <a href="tareas?action=listar">Volver a la lista</a>
</body>
</html>
