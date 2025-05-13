package services;

import models.Employee;
import models.Show;
import models.Ticket;

import java.io.IOException;
import java.util.List;

public interface IServices {
    Employee login(String username, String password, IObserver client) throws Exception;

    void logout(Employee employee, IObserver client) throws ServiceException, IOException;

    List<Show> getAllShows() throws ServiceException;

    void sellTicket(String buyerName, int numberOfSeats, Show show) throws ServiceException;

    List<Ticket> getTicketsForShow(Show show) throws ServiceException;
}