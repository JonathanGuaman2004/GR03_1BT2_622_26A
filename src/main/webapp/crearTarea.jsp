<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Crear Tarea</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<div class="container">
    <h1>Crear Nueva Tarea</h1>

    <form action="${pageContext.request.contextPath}/tareas" method="post">
        <input type="hidden" name="action" value="guardar">

        <div class="form-group">
            <label for="titulo">Título:</label>
            <input type="text" id="titulo" name="titulo"
                   placeholder="Ej: Revisar documentación" required maxlength="150">
        </div>

        <div class="form-group">
            <label for="descripcion">Descripción:</label>
            <textarea id="descripcion" name="descripcion"
                      placeholder="Describe la tarea..." required maxlength="500" rows="4"></textarea>
        </div>

        <div class="form-group">
            <label for="estado">Estado:</label>
            <select id="estado" name="estado">
                <option value="Pendiente">Pendiente</option>
                <option value="En Progreso">En Progreso</option>
                <option value="Completada">Completada</option>
            </select>
        </div>

        <div class="form-actions">
            <input type="submit" class="btn" value="Guardar">
            <a class="btn btn-cancelar" href="${pageContext.request.contextPath}/tareas?action=listar">Cancelar</a>
        </div>
    </form>
</div>
</body>
</html>
