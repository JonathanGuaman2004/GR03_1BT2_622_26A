<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Editar Tarea</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
<div class="container">
    <h1>Editar Tarea</h1>

    <c:choose>
        <c:when test="${empty tarea}">
            <p class="error">Tarea no encontrada.</p>
            <a class="btn" href="${pageContext.request.contextPath}/tareas?action=listar">Volver</a>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/tareas" method="post">
                <input type="hidden" name="action" value="actualizar">
                <input type="hidden" name="id"     value="${tarea.id}">

                <div class="form-group">
                    <label for="titulo">Título:</label>
                    <input type="text" id="titulo" name="titulo"
                           value="<c:out value="${tarea.titulo}"/>"
                           required maxlength="150">
                </div>

                <div class="form-group">
                    <label for="descripcion">Descripción:</label>
                    <textarea id="descripcion" name="descripcion"
                              required maxlength="500" rows="4"><c:out value="${tarea.descripcion}"/></textarea>
                </div>

                <div class="form-group">
                    <label for="estado">Estado:</label>
                    <select id="estado" name="estado">
                        <option value="Pendiente"   <c:if test="${tarea.estado == 'Pendiente'}">selected</c:if>>Pendiente</option>
                        <option value="En Progreso" <c:if test="${tarea.estado == 'En Progreso'}">selected</c:if>>En Progreso</option>
                        <option value="Completada"  <c:if test="${tarea.estado == 'Completada'}">selected</c:if>>Completada</option>
                    </select>
                </div>

                <div class="form-actions">
                    <input type="submit" class="btn" value="Actualizar">
                    <a class="btn btn-cancelar" href="${pageContext.request.contextPath}/tareas?action=listar">Cancelar</a>
                </div>
            </form>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
