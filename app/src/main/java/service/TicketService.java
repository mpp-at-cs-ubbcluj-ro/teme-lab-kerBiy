package service;

import model.Show;
import model.Ticket;
import repository.ShowRepository;
import repository.TicketRepository;

public class TicketService {
    private final TicketRepository ticketRepository;
    private final ShowRepository showRepository;

    public TicketService(TicketRepository ticketRepository, ShowRepository showRepository) {
        this.ticketRepository = ticketRepository;
        this.showRepository = showRepository;
    }

    public boolean sellTicket(String buyerName, int nrSeats, Show show) {
        if (show.getSoldSeats() + nrSeats > show.getTotalSeats()) {
            return false;
        }

        Ticket ticket = new Ticket(buyerName, nrSeats, show.getId());
        ticketRepository.save(ticket);

        show.setSoldSeats(show.getSoldSeats() + nrSeats);
        showRepository.update(show);
        return true;
    }
}