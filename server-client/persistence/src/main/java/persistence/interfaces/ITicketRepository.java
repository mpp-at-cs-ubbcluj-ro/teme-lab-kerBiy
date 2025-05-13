package persistence.interfaces;

import models.Ticket;

import java.util.List;

public interface ITicketRepository extends ICrudRepository<Long, Ticket> {
    List<Ticket> findByShowId(Long showId);
}
