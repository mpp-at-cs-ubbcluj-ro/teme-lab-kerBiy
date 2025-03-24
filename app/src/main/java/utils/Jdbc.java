package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Jdbc {

    private final Properties jdbcProps;
    private static final Logger logger = LogManager.getLogger();
    private Connection instance = null;

    public Jdbc(Properties props) {
        this.jdbcProps = props;
        this.instance = createNewConnection();
    }

    private Connection createNewConnection() {
        logger.traceEntry();

        String url = jdbcProps.getProperty("jdbc.url");
        String user = jdbcProps.getProperty("jdbc.user");
        String pass = jdbcProps.getProperty("jdbc.pass");

        logger.info("trying to connect to database ... {}", url);
        logger.info("user: {}", user);
        logger.info("pass: {}", pass);

        try {
            if (user != null && pass != null && !user.isEmpty())
                return DriverManager.getConnection(url, user, pass);
            else
                return DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.error("Error getting connection: ", e);
            throw new RuntimeException("Error creating DB connection: " + e.getMessage(), e);
        }
    }

    public Connection getConnection() {
        try {
            if (instance == null || instance.isClosed()) {
                logger.warn("Connection was closed, recreating...");
                instance = createNewConnection();
            }
        } catch (SQLException e) {
            logger.error("Error checking connection state: ", e);
            throw new RuntimeException("Failed to access connection", e);
        }
        return instance;
    }
}