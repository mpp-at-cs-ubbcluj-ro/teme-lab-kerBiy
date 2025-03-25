package service;

import model.Employee;
import model.Show;

import java.time.LocalDate;
import java.util.List;

public class Service {
    private final ShowService showService;
    private final TicketService ticketService;
    private final EmployeeService employeeService;

    public Service(ShowService showService, TicketService ticketService, EmployeeService employeeService) {
        this.showService = showService;
        this.ticketService = ticketService;
        this.employeeService = employeeService;
    }

    // === Employee related ===
    public Employee login(String username, String password) {
        return employeeService.login(username, password);
    }

    // === Show related ===
    public List<Show> getAllShows() {
        return showService.getAllShows();
    }

    public List<Show> searchShowsByDate(LocalDate date) {
        return showService.searchByDate(date);
    }

    public void updateShow(Show show) {
        showService.updateShow(show);
    }

    // === Ticket related ===
    public boolean sellTicket(String buyerName, int nrSeats, Show show) {
        return ticketService.sellTicket(buyerName, nrSeats, show);
    }
}