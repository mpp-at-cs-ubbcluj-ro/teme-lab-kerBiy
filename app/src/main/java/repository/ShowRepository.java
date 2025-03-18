package repository;

import model.Show;
import utils.Jdbc;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowRepository implements IShowRepository {
    private static final Logger logger = LogManager.getLogger();
    private final Jdbc jdbc;

    public ShowRepository(Jdbc jdbc) {
        logger.info("Initializing ShowRepository");
        this.jdbc = jdbc;
    }

    @Override
    public Optional<Show> findOne(Long id) {
        logger.traceEntry("findOne({})", id);
        String query = "SELECT * FROM shows WHERE id = ?";
        try (Connection con = jdbc.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Show show = extractShow(rs);
                    logger.traceExit(show);
                    return Optional.of(show);
                }
            }
        } catch (SQLException e) {
            logger.error("Database error: ", e);
        }
        logger.traceExit("No show found");
        return Optional.empty();
    }

    @Override
    public Iterable<Show> findAll() {
        logger.traceEntry();
        List<Show> shows = new ArrayList<>();
        String query = "SELECT * FROM shows";
        try (Connection con = jdbc.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                shows.add(extractShow(rs));
            }
        } catch (SQLException e) {
            logger.error("Database error: ", e);
        }
        logger.traceExit(shows);
        return shows;
    }

    @Override
    public Optional<Show> save(Show entity) {
        String query = "INSERT INTO shows (artist, date, location, totalSeats, soldSeats) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = jdbc.getConnection();
             PreparedStatement stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, entity.getArtist());
            stmt.setDate(2, new java.sql.Date(entity.getDate().getTime()));
            stmt.setString(3, entity.getLocation());
            stmt.setInt(4, entity.getTotalSeats());
            stmt.setInt(5, entity.getSoldSeats());
            int result = stmt.executeUpdate();
            if (result > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        entity.setId(rs.getLong(1));
                    }
                }
                return Optional.of(entity);
            }
        } catch (SQLException e) {
            logger.error("Database error: ", e);
        }
        logger.traceExit("Save failed");
        return Optional.empty();
    }

    @Override
    public Optional<Show> delete(Long id) {
        logger.traceEntry("delete({})", id);
        Optional<Show> show = findOne(id);
        if (show.isPresent()) {
            String query = "DELETE FROM shows WHERE id = ?";
            try (Connection con = jdbc.getConnection();
                 PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setLong(1, id);
                int result = stmt.executeUpdate();
                if (result > 0) {
                    logger.traceExit(show);
                    return show;
                }
            } catch (SQLException e) {
                logger.error("Database error: ", e);
            }
        }
        logger.traceExit("Delete failed");
        return Optional.empty();
    }

    @Override
    public Optional<Show> update(Show entity) {
        logger.traceEntry("update({})", entity);
        String query = "UPDATE shows SET artist = ?, date = ?, location = ?, totalSeats = ?, soldSeats = ? WHERE id = ?";
        try (Connection con = jdbc.getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, entity.getArtist());
            stmt.setDate(2, new java.sql.Date(entity.getDate().getTime()));
            stmt.setString(3, entity.getLocation());
            stmt.setInt(4, entity.getTotalSeats());
            stmt.setInt(5, entity.getSoldSeats());
            stmt.setLong(6, entity.getId());
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

    private Show extractShow(ResultSet rs) throws SQLException {
        Show show = new Show(
                rs.getString("artist"),
                rs.getDate("date"),
                rs.getString("location"),
                rs.getInt("totalSeats")
        );
        show.setId(rs.getLong("id"));
        return show;
    }
}
