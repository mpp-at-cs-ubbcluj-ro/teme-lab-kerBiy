package network.protocol;

import network.dto.EmployeeDTO;
import network.dto.ShowDTO;
import network.dto.TicketDTO;

public class Request {
    private RequestType type;
    private EmployeeDTO employee;
    private ShowDTO show;
    private TicketDTO ticket;

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
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

    public TicketDTO getTicket() {
        return ticket;
    }

    public void setTicket(TicketDTO ticket) {
        this.ticket = ticket;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", employee=" + employee +
                ", show=" + show +
                ", ticket=" + ticket +
                '}';
    }
}