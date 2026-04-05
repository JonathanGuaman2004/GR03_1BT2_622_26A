package com.webapp.gr03_1bt2_622_26a;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "TareaServlet", value = "/tareas")
public class TareaServlet extends HttpServlet {
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "listar";

        // Endpoint de prueba para verificar BD
        if ("test".equals(action)) {
            testDatabase(request, response);
            return;
        }

        switch (action) {
            case "listar":
                listarTareas(request, response);
                break;
            case "crear":
                mostrarCrearTarea(request, response);
                break;
            case "editar":
                mostrarEditarTarea(request, response);
                break;
            case "eliminar":
                eliminarTarea(request, response);
                break;
            default:
                listarTareas(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("guardar".equals(action)) {
            guardarTarea(request, response);
        } else if ("actualizar".equals(action)) {
            actualizarTarea(request, response);
        }
    }

    private void listarTareas(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Tarea> tareas = tareaDAO.listar();
        request.setAttribute("tareas", tareas);
        request.getRequestDispatcher("listaTareas.jsp").forward(request, response);
    }

    private void mostrarCrearTarea(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("crearTarea.jsp").forward(request, response);
    }

    private void mostrarEditarTarea(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                Tarea tarea = tareaDAO.obtenerPorId(id);
                request.setAttribute("tarea", tarea);
            }
            request.getRequestDispatcher("editarTarea.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (Exception e) {
            System.err.println("Error al mostrar editar tarea: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar la solicitud");
        }
    }

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

    private void actualizarTarea(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            String titulo = request.getParameter("titulo");
            String descripcion = request.getParameter("descripcion");
            String estado = request.getParameter("estado");
            Tarea tarea = new Tarea(titulo, descripcion, estado);
            tarea.setId(id);
            tareaDAO.actualizar(tarea);
            response.sendRedirect("tareas?action=listar");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (Exception e) {
            System.err.println("Error al actualizar tarea: " + e.getMessage());
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al actualizar la tarea");
            } catch (IllegalStateException ignored) {}
        }
    }

    private void eliminarTarea(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                int id = Integer.parseInt(idParam);
                tareaDAO.eliminar(id);
            }
            response.sendRedirect("tareas?action=listar");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
        } catch (Exception e) {
            System.err.println("Error al eliminar tarea: " + e.getMessage());
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al eliminar la tarea");
            } catch (IllegalStateException ignored) {}
        }
    }

    private void testDatabase(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // Lógica para probar la conexión a la base de datos
            // Esto puede ser tan simple como intentar obtener una conexión y cerrarla,
            // o una consulta más compleja dependiendo de lo que necesites verificar.

            // Ejemplo simple:
            tareaDAO.listar(); // Intenta listar tareas como prueba de conexión

            response.getWriter().write("Conexión a la base de datos exitosa.");
        } catch (Exception e) {
            System.err.println("Error en la prueba de conexión a la base de datos: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error en la prueba de conexión a la base de datos");
        }
    }
}
