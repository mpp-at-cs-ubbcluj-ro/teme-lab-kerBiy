package repository;

import model.Ticket;
import utils.Jdbc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketRepository implements ITicketRepository {
    private static final Logger logger = LogManager.getLogger();
    private final Jdbc jdbc;

    public TicketRepository(Jdbc jdbc) {
        logger.info("Initializing TicketRepository");
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Ticket> findOne(Long id) {
        logger.traceEntry("findOne({})", id);
        String query = "SELECT * FROM tickets WHERE id = ?";
        try (Connection con = jdbc.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Ticket ticket = extractTicket(rs);
                    logger.traceExit(ticket);
                    return Optional.of(ticket);
                }
            }
        } catch (SQLException e) {
            logger.error("Database error: ", e);
        }
        logger.traceExit("No ticket found");
        return Optional.empty();
    }

    @Override
    public Iterable<Ticket> findAll() {
        logger.traceEntry();
        List<Ticket> tickets = new ArrayList<>();
        String query = "SELECT * FROM tickets";
        try (Connection con = jdbc.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                tickets.add(extractTicket(rs));
            }
        } catch (SQLException e) {
            logger.error("Database error: ", e);
        }
        logger.traceExit(tickets);
        return tickets;
    }

    @Override
    public Optional<Ticket> save(Ticket entity) {
        logger.traceEntry("save({})", entity);
        String query = "INSERT INTO tickets (name, numberOfSeats, showId) VALUES (?, ?, ?)";
        try (Connection con = jdbc.getConnection();
             PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getName());
            stmt.setInt(2, entity.getNumberOfSeats());
            stmt.setLong(3, entity.getShowId());
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
    public Optional<Ticket> delete(Long id) {
        logger.traceEntry("delete({})", id);
        Optional<Ticket> ticket = findOne(id);
        if (ticket.isPresent()) {
            String query = "DELETE FROM tickets WHERE id = ?";
            try (Connection con = jdbc.getConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setLong(1, id);
                int result = stmt.executeUpdate();
                if (result > 0) {
                    logger.traceExit(ticket);
                    return ticket;
                }
            } catch (SQLException e) {
                logger.error("Database error: ", e);
            }
        }
        logger.traceExit("Delete failed");
        return Optional.empty();
    }

    @Override
    public Optional<Ticket> update(Ticket entity) {
        logger.traceEntry("update({})", entity);
        if (entity.getId() == null) {
            logger.error("Cannot update Ticket with null ID");
            return Optional.empty();
        }
        String query = "UPDATE tickets SET name = ?, numberOfSeats = ?, showId = ? WHERE id = ?";
        try (Connection con = jdbc.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, entity.getName());
            stmt.setInt(2, entity.getNumberOfSeats());
            stmt.setLong(3, entity.getShowId());
            stmt.setLong(4, entity.getId());
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

    private Ticket extractTicket(ResultSet rs) throws SQLException {
        Ticket ticket = new Ticket(
                rs.getString("name"),
                rs.getInt("numberOfSeats"),
                rs.getLong("showId")
        );
        ticket.setId(rs.getLong("id"));
        return ticket;
    }
}
