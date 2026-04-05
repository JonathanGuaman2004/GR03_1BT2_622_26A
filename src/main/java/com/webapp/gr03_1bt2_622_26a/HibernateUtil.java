package com.webapp.gr03_1bt2_622_26a;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.io.File;

/**
 * Crea y mantiene el SessionFactory de Hibernate como singleton.
 *
 * La ruta de la BD se calcula en tiempo de ejecución a partir del
 * directorio real de la webapp (WEB-INF/tareas.db), evitando rutas
 * hardcodeadas o variables de entorno que pueden fallar en Windows.
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    /** Llama esto UNA SOLA VEZ desde AppListener.contextInitialized(). */
    public static synchronized void init(String webAppRealPath) {
        if (sessionFactory != null) return;

        // WEB-INF/tareas.db  — SQLite crea el archivo si no existe,
        // pero el directorio ya debe existir (WEB-INF siempre existe).
        File dbFile = new File(webAppRealPath, "WEB-INF/tareas.db");
        String url  = "jdbc:sqlite:" + dbFile.getAbsolutePath();

        System.out.println("[HibernateUtil] Ruta de BD: " + url);

        sessionFactory = new Configuration()
                .configure()                          // lee hibernate.cfg.xml
                .setProperty(Environment.URL, url)    // sobreescribe la URL del XML
                .buildSessionFactory();
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException(
                    "SessionFactory no inicializado. ¿Se registró AppListener en web.xml?");
        }
        return sessionFactory;
    }

    public static synchronized void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }
}