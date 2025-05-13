package persistence.repositories;

import models.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.interfaces.IEmployeeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeRepositoryJdbc implements IEmployeeRepository {
    private static final Logger logger = LogManager.getLogger();
    private final JdbcUtils jdbc;

    public EmployeeRepositoryJdbc(JdbcUtils jdbc) {
        logger.info("Initializing EmployeeRepository");
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Employee> findOne(Long id) {
        logger.traceEntry("findOne({})", id);
        String query = "SELECT * FROM employees WHERE id = ?";
        try (Connection con = jdbc.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Employee employee = extractEmployee(rs);
                    logger.traceExit(employee);
                    return Optional.of(employee);
                }
            }
        } catch (SQLException e) {
            logger.error("Database error: ", e);
        }
        logger.traceExit("No employee found");
        return Optional.empty();
    }

    @Override
    public Iterable<Employee> findAll() {
        logger.traceEntry();
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employees";
        try (Connection con = jdbc.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                employees.add(extractEmployee(rs));
            }
        } catch (SQLException e) {
            logger.error("Database error: ", e);
        }
        logger.traceExit(employees);
        return employees;
    }

    @Override
    public Optional<Employee> save(Employee entity) {
        logger.traceEntry("save({})", entity);
        String query = "INSERT INTO employees (username, password) VALUES (?, ?)";
        try (Connection con = jdbc.getConnection();
             PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getUsername());
            stmt.setString(2, entity.getPassword());
            int result = stmt.executeUpdate();
            if (result > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getLong(1));
                    }
                }
                logger.traceExit(entity);
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Database error: ", e);
        }
        logger.traceExit("Save failed");
        return Optional.empty();
    }

    @Override
    public Optional<Employee> delete(Long id) {
        logger.traceEntry("delete({})", id);
        Optional<Employee> employee = findOne(id);
        if (employee.isPresent()) {
            String query = "DELETE FROM employees WHERE id = ?";
            try (Connection con = jdbc.getConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setLong(1, id);
                int result = stmt.executeUpdate();
                if (result > 0) {
                    logger.traceExit(employee);
                    return employee;
                }
            } catch (SQLException e) {
                logger.error("Database error: ", e);
            }
        }
        logger.traceExit("Delete failed");
        return Optional.empty();
    }

    @Override
    public Optional<Employee> update(Employee entity) {
        logger.traceEntry("update({})", entity);
        String query = "UPDATE employees SET username = ?, password = ? WHERE id = ?";
        try (Connection con = jdbc.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, entity.getUsername());
            stmt.setString(2, entity.getPassword());
            stmt.setLong(3, entity.getId());
            int updated = stmt.executeUpdate();
            if (updated > 0) {
                logger.traceExit(entity);
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Database error: ", e);
        }
        logger.traceExit("Update failed");
        return Optional.empty();
    }

    private Employee extractEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee(rs.getString("username"), rs.getString("password"));
        employee.setId(rs.getLong("id"));
        return employee;
    }

    @Override
    public Optional<Employee> findBy(String username, String password) {
        logger.traceEntry("findBy({}, {})", username, password);
        String query = "SELECT * FROM employees WHERE username = ? AND password = ?";
        try (Connection con = jdbc.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Long id = rs.getLong("id");
                    Employee emp = new Employee(username, password);
                    emp.setId(id);
                    return Optional.of(emp);
                }
            }
        } catch (SQLException e) {
            logger.error("Error in findBy: {}", e.getMessage());
        }
        return Optional.empty();
    }
}