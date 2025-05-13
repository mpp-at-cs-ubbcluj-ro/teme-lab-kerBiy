package network.dto;

import models.Employee;
import models.Show;
import models.Ticket;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DTOUtils {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public static EmployeeDTO employeeToDTO(Employee employee) {
        return new EmployeeDTO(employee.getId(), employee.getUsername());
    }

    public static Employee dtoToEmployee(EmployeeDTO dto) {
        return new Employee(dto.username, dto.password);
    }

    public static ShowDTO showToDTO(Show show) {
        return new ShowDTO(
                show.getId(),
                show.getArtist(),
                formatter.format(show.getDate()),
                show.getLocation(),
                show.getTotalSeats(),
                show.getSoldSeats()
        );
    }

    public static Show dtoToShow(ShowDTO dto) {
        try {
            Date date = formatter.parse(dto.date);
            Show show = new Show(dto.artist, date, dto.location, dto.totalSeats, dto.soldSeats);
            show.setId(dto.id);
            return show;
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format: " + dto.date, e);
        }
    }

    public static TicketDTO ticketToDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getName(),
                ticket.getNumberOfSeats(),
                ticket.getShowId()
        );
    }

    public static Ticket dtoToTicket(TicketDTO dto) {
        return new Ticket(dto.name, dto.numberOfSeats, dto.showId);
    }
}