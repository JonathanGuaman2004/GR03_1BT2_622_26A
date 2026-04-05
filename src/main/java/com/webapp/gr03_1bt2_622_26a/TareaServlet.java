package com.webapp.gr03_1bt2_622_26a;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

/**
 * Capa de Control — maneja las acciones CRUD sobre Tarea.
 * URL base: /tareas?action=...
 */
@WebServlet(name = "TareaServlet", urlPatterns = "/tareas")
public class TareaServlet extends HttpServlet {

    private TareaDAO tareaDAO;

    @Override
    public void init() throws ServletException {
        tareaDAO = new TareaDAO();
    }

    // ── GET: listar, mostrar formulario crear/editar, eliminar ───────────────
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        if (action == null) action = "listar";

        switch (action) {
            case "listar"  -> listar(req, res);
            case "crear"   -> forward(req, res, "/crearTarea.jsp");
            case "editar"  -> mostrarEditar(req, res);
            case "eliminar"-> eliminar(req, res);
            default        -> listar(req, res);
        }
    }

    // ── POST: guardar nueva tarea o actualizar existente ─────────────────────
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        switch (action != null ? action : "") {
            case "guardar"    -> guardar(req, res);
            case "actualizar" -> actualizar(req, res);
            default           -> res.sendRedirect(req.getContextPath() + "/tareas?action=listar");
        }
    }

    // ── Acciones privadas ─────────────────────────────────────────────────────

    private void listar(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        List<Tarea> tareas = tareaDAO.listar();
        req.setAttribute("tareas", tareas);
        forward(req, res, "/listaTareas.jsp");
    }

    private void mostrarEditar(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int id = parseId(req, res);
        if (id < 0) return;                       // parseId ya envió el error

        Tarea tarea = tareaDAO.obtenerPorId(id);
        if (tarea == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "Tarea no encontrada (id=" + id + ")");
            return;
        }
        req.setAttribute("tarea", tarea);
        forward(req, res, "/editarTarea.jsp");
    }

    private void guardar(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String titulo      = req.getParameter("titulo");
        String descripcion = req.getParameter("descripcion");
        String estado      = req.getParameter("estado");

        tareaDAO.guardar(new Tarea(titulo, descripcion, estado));
        res.sendRedirect(req.getContextPath() + "/tareas?action=listar");
    }

    private void actualizar(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        int id = parseId(req, res);
        if (id < 0) return;

        String titulo      = req.getParameter("titulo");
        String descripcion = req.getParameter("descripcion");
        String estado      = req.getParameter("estado");

        Tarea tarea = new Tarea(titulo, descripcion, estado);
        tarea.setId(id);
        tareaDAO.actualizar(tarea);
        res.sendRedirect(req.getContextPath() + "/tareas?action=listar");
    }

    private void eliminar(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        int id = parseId(req, res);
        if (id < 0) return;

        tareaDAO.eliminar(id);
        res.sendRedirect(req.getContextPath() + "/tareas?action=listar");
    }

    // ── Utilidades ────────────────────────────────────────────────────────────

    /** Parsea el parámetro "id". Devuelve -1 y escribe 400 si falla. */
    private int parseId(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        String param = req.getParameter("id");
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido: " + param);
            return -1;
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse res, String jsp)
            throws ServletException, IOException {
        req.getRequestDispatcher(jsp).forward(req, res);
    }
}