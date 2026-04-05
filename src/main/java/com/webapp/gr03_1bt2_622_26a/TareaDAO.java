package com.webapp.gr03_1bt2_622_26a;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.util.List;
import java.util.ArrayList;

public class TareaDAO {
    private static SessionFactory sessionFactory;
    private static volatile boolean initialized = false;

    private static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null && !initialized) {
            try {
                sessionFactory = new Configuration().configure().buildSessionFactory();
                initialized = true;
            } catch (Exception e) {
                initialized = true;
                System.err.println("Error initializing Hibernate SessionFactory: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error initializing Hibernate SessionFactory", e);
            }
        }
        return sessionFactory;
    }

    public void guardar(Tarea tarea) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                session.persist(tarea);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println("Error al guardar tarea: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Tarea> listar() {
        try (Session session = getSessionFactory().openSession()) {
            return session.createQuery("from Tarea", Tarea.class).list();
        } catch (Exception e) {
            System.err.println("Error al listar tareas: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void actualizar(Tarea tarea) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                session.merge(tarea);
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println("Error al actualizar tarea: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminar(int id) {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                Tarea tarea = session.get(Tarea.class, id);
                if (tarea != null) {
                    session.remove(tarea);
                }
                session.getTransaction().commit();
            } catch (Exception e) {
                session.getTransaction().rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println("Error al eliminar tarea: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Tarea obtenerPorId(int id) {
        try (Session session = getSessionFactory().openSession()) {
            return session.get(Tarea.class, id);
        } catch (Exception e) {
            System.err.println("Error al obtener tarea por id: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
