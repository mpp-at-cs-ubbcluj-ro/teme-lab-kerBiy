package server;

import models.Employee;
import models.Show;
import models.Ticket;
import persistence.interfaces.IEmployeeRepository;
import persistence.interfaces.IShowRepository;
import persistence.interfaces.ITicketRepository;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceImpl implements IServices {

    private final IEmployeeRepository employeeRepo;
    private final IShowRepository showRepo;
    private final ITicketRepository ticketRepo;

    // Observerii clienților conectați (key = id angajat)
    private final Map<Long, IObserver> loggedClients = new ConcurrentHashMap<>();

    public ServiceImpl(IEmployeeRepository empRepo, IShowRepository showRepo, ITicketRepository ticketRepo) {
        this.employeeRepo = empRepo;
        this.showRepo = showRepo;
        this.ticketRepo = ticketRepo;
    }

    @Override
    public synchronized Employee login(String username, String password, IObserver client) throws ServiceException {
        Employee emp = employeeRepo.findBy(username, password)
                .orElseThrow(() -> new ServiceException("Autentificare eșuată!"));
        if (emp == null)
            throw new ServiceException("Autentificare eșuată!");
        if (loggedClients.containsKey(emp.getId()))
            throw new ServiceException("Utilizator deja conectat!");
        loggedClients.put(emp.getId(), client);
        return emp;
    }

    @Override
    public synchronized void logout(Employee employee, IObserver client) throws ServiceException {
        if (!loggedClients.containsKey(employee.getId()))
            throw new ServiceException("Utilizatorul nu este conectat!");
        loggedClients.remove(employee.getId());
    }

    @Override
    public synchronized List<Show> getAllShows() throws ServiceException {
        return (List<Show>) showRepo.findAll();
    }

    @Override
    public synchronized void sellTicket(String buyerName, int numberOfSeats, Show show) throws ServiceException {
        if (numberOfSeats <= 0)
            throw new ServiceException("Numărul de locuri trebuie să fie pozitiv.");

        if (show.getSoldSeats() + numberOfSeats > show.getTotalSeats())
            throw new ServiceException("Nu sunt suficiente locuri disponibile.");

        Ticket ticket = new Ticket(buyerName, numberOfSeats, show.getId());
        ticketRepo.save(ticket);

        // Update seats
        show.setSoldSeats(show.getSoldSeats() + numberOfSeats);
        showRepo.update(show);

        // Notificăm toți clienții conectați
        notifyClients(show);
    }

    @Override
    public synchronized List<Ticket> getTicketsForShow(Show show) throws ServiceException {
        return ticketRepo.findByShowId(show.getId());
    }

    private void notifyClients(Show updatedShow) {
        for (IObserver observer : loggedClients.values()) {
            try {
                observer.showUpdated(updatedShow);
            } catch (Exception e) {
                System.err.println("Eroare la notificare observer: " + e.getMessage());
            }
        }
    }
}