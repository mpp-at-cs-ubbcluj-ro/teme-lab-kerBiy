package controller;


import service.ShowService;
import service.TicketService;
import service.EmployeeService;

import java.util.Optional;

public abstract class GenericController {
    protected ShowService showService;
    protected EmployeeService employeeService;
    protected TicketService ticketService;

    public void setService(ShowService showService, EmployeeService employeeService, TicketService ticketService) {
        this.showService = showService;
        this.employeeService = employeeService;
        this.ticketService = ticketService;
    }

    public abstract void setSomething(Optional<Object> parameter);
}