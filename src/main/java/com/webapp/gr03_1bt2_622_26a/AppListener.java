package com.webapp.gr03_1bt2_622_26a;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Se ejecuta automáticamente cuando Tomcat despliega la webapp.
 * Inicializa Hibernate con la ruta real del disco y lo cierra al terminar.
 */
@WebListener
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        // getRealPath("/") devuelve la ruta absoluta de la webapp en disco,
        // ej: C:\apache-tomcat-10.1.54\webapps\GR03_1BT2_622_26A\
        String realPath = ctx.getRealPath("/");

        if (realPath == null) {
            throw new RuntimeException(
                    "No se pudo obtener la ruta real de la webapp. " +
                            "Verifica que Tomcat no esté ejecutando desde un WAR comprimido.");
        }

        System.out.println("[AppListener] Inicializando Hibernate. webAppPath=" + realPath);
        HibernateUtil.init(realPath);
        System.out.println("[AppListener] Hibernate inicializado correctamente.");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("[AppListener] Cerrando Hibernate SessionFactory...");
        HibernateUtil.shutdown();
    }
}