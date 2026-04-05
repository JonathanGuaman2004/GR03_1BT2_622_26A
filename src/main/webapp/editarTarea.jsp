<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.webapp.gr03_1bt2_622_26a.Tarea" %>
<html>
<head>
    <title>Editar Tarea</title>
</head>
<body>
    <h1>Editar Tarea</h1>
    <%
        Tarea tarea = (Tarea) request.getAttribute("tarea");
        if (tarea != null) {
    %>
    <form action="tareas" method="post">
        <input type="hidden" name="action" value="actualizar">
        <input type="hidden" name="id" value="<%= tarea.getId() %>">
        <label for="titulo">Título:</label>
        <input type="text" id="titulo" name="titulo" value="<%= tarea.getTitulo() %>" required><br><br>
        <label for="descripcion">Descripción:</label>
        <textarea id="descripcion" name="descripcion" required><%= tarea.getDescripcion() %></textarea><br><br>
        <label for="estado">Estado:</label>
        <select id="estado" name="estado">
            <option value="Pendiente" <%= "Pendiente".equals(tarea.getEstado()) ? "selected" : "" %>>Pendiente</option>
            <option value="En Progreso" <%= "En Progreso".equals(tarea.getEstado()) ? "selected" : "" %>>En Progreso</option>
            <option value="Completada" <%= "Completada".equals(tarea.getEstado()) ? "selected" : "" %>>Completada</option>
        </select><br><br>
        <input type="submit" value="Actualizar">
    </form>
    <%
        }
    %>
    <a href="tareas?action=listar">Volver a la lista</a>
</body>
</html>
