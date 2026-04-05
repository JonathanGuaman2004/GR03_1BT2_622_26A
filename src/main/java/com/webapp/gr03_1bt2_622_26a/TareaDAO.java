package com.webapp.gr03_1bt2_622_26a;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collections;
import java.util.List;

/**
 * Capa de Datos — CRUD de Tarea usando Hibernate.
 * El SessionFactory lo administra HibernateUtil (inicializado por AppListener).
 */
public class TareaDAO {

    private SessionFactory sf() {
        return HibernateUtil.getSessionFactory();
    }

    /** Persiste una nueva tarea. */
    public void guardar(Tarea tarea) {
        enTransaccion(session -> session.persist(tarea));
    }

    /** Lista todas las tareas ordenadas de más reciente a más antigua. */
    public List<Tarea> listar() {
        try (Session session = sf().openSession()) {
            return session.createQuery("FROM Tarea ORDER BY id DESC", Tarea.class).list();
        } catch (Exception e) {
            System.err.println("Error al listar tareas: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    /** Busca una tarea por ID. Devuelve null si no existe. */
    public Tarea obtenerPorId(int id) {
        try (Session session = sf().openSession()) {
            return session.get(Tarea.class, id);
        } catch (Exception e) {
            System.err.println("Error al obtener tarea id=" + id + ": " + e.getMessage());
            return null;
        }
    }

    /** Actualiza los datos de una tarea existente. */
    public void actualizar(Tarea tarea) {
        enTransaccion(session -> session.merge(tarea));
    }

    /** Elimina la tarea con el id indicado (no hace nada si no existe). */
    public void eliminar(int id) {
        enTransaccion(session -> {
            Tarea tarea = session.get(Tarea.class, id);
            if (tarea != null) session.remove(tarea);
        });
    }

    // ── Utilitario ────────────────────────────────────────────────────────────
    private void enTransaccion(AccionSession accion) {
        try (Session session = sf().openSession()) {
            session.beginTransaction();
            try {
                accion.ejecutar(session);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                System.err.println("Transacción revertida: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    @FunctionalInterface
    private interface AccionSession {
        void ejecutar(Session session);
    }
}