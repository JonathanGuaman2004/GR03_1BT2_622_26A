<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Lista de Tareas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<div class="container">
    <h1>Sistema Web de Gestión de Tareas</h1>

    <a class="btn" href="${pageContext.request.contextPath}/tareas?action=crear">+ Nueva Tarea</a>

    <c:choose>
        <c:when test="${empty tareas}">
            <p class="info">No hay tareas registradas aún.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Título</th>
                    <th>Descripción</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="tarea" items="${tareas}">
                    <tr>
                        <td><c:out value="${tarea.id}"/></td>
                        <td><c:out value="${tarea.titulo}"/></td>
                        <td><c:out value="${tarea.descripcion}"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${tarea.estado == 'Pendiente'}">
                                    <span class="estado estado-pendiente"><c:out value="${tarea.estado}"/></span>
                                </c:when>
                                <c:when test="${tarea.estado == 'En Progreso'}">
                                    <span class="estado estado-progreso"><c:out value="${tarea.estado}"/></span>
                                </c:when>
                                <c:otherwise>
                                    <span class="estado estado-completada"><c:out value="${tarea.estado}"/></span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td class="acciones">
                            <a class="btn btn-sm btn-editar"
                               href="${pageContext.request.contextPath}/tareas?action=editar&id=${tarea.id}">
                                Editar
                            </a>
                            <a class="btn btn-sm btn-eliminar"
                               href="${pageContext.request.contextPath}/tareas?action=eliminar&id=${tarea.id}"
                               onclick="return confirm('¿Eliminar esta tarea?')">
                                Eliminar
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
