package persistence.hibernate;

import models.Employee;
import models.Show;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;
    private static String jdbcUrl;

    public static void setJdbcUrl(String url) {
        jdbcUrl = url;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            if (jdbcUrl == null) {
                throw new IllegalStateException("JDBC URL was not set before initializing SessionFactory.");
            }

            try {
                Configuration cfg = new Configuration().configure(); // încarcă hibernate.cfg.xml

                cfg.setProperty("hibernate.connection.url", jdbcUrl);

                cfg.addAnnotatedClass(Employee.class);
                cfg.addAnnotatedClass(Show.class);

                sessionFactory = cfg.buildSessionFactory(
                        new StandardServiceRegistryBuilder()
                                .applySettings(cfg.getProperties())
                                .build()
                );
            } catch (Exception ex) {
                System.err.println("Initial SessionFactory creation failed: " + ex);
                throw new ExceptionInInitializerError(ex);
            }
        }

        return sessionFactory;
    }
}