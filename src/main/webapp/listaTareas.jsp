<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.webapp.gr03_1bt2_622_26a.Tarea" %>
<html>
<head>
    <title>Lista de Tareas</title>
</head>
<body>
    <h1>Sistema Web de Gestión de Tareas</h1>
    <a href="tareas?action=crear">Crear Nueva Tarea</a>
    <table border="1">
        <tr>
            <th>ID</th>
            <th>Título</th>
            <th>Descripción</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
        <%
            List<Tarea> tareas = (List<Tarea>) request.getAttribute("tareas");
            if (tareas != null) {
                for (Tarea tarea : tareas) {
        %>
        <tr>
            <td><%= tarea.getId() %></td>
            <td><%= tarea.getTitulo() %></td>
            <td><%= tarea.getDescripcion() %></td>
            <td><%= tarea.getEstado() %></td>
            <td>
                <a href="tareas?action=editar&id=<%= tarea.getId() %>">Editar</a>
                <a href="tareas?action=eliminar&id=<%= tarea.getId() %>">Eliminar</a>
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
