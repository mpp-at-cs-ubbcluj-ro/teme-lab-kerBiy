package network.protocol;

import network.dto.EmployeeDTO;
import network.dto.ShowDTO;
import network.dto.TicketDTO;

import java.util.List;

public class Response {
    private ResponseType type;
    private String error;
    private EmployeeDTO employee;
    private ShowDTO show;
    private List<ShowDTO> shows;
    private List<TicketDTO> tickets;

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public ShowDTO getShow() {
        return show;
    }

    public void setShow(ShowDTO show) {
        this.show = show;
    }

    public List<ShowDTO> getShows() {
        return shows;
    }

    public void setShows(List<ShowDTO> shows) {
        this.shows = shows;
    }

    public List<TicketDTO> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketDTO> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", error='" + error + '\'' +
                ", employee=" + employee +
                ", show=" + show +
                ", shows=" + shows +
                ", tickets=" + tickets +
                '}';
    }
}