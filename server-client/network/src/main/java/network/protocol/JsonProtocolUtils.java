package network.protocol;

import network.dto.EmployeeDTO;
import network.dto.ShowDTO;
import network.dto.TicketDTO;

import java.util.List;

public class JsonProtocolUtils {
    public static Request createLoginRequest(EmployeeDTO dto) {
        Request r = new Request();
        r.setType(RequestType.LOGIN);
        r.setEmployee(dto);
        return r;
    }

    public static Request createLogoutRequest(EmployeeDTO dto) {
        Request r = new Request();
        r.setType(RequestType.LOGOUT);
        r.setEmployee(dto);
        return r;
    }

    public static Request createSimpleRequest(RequestType type) {
        Request r = new Request();
        r.setType(type);
        return r;
    }

    public static Request createSellTicketRequest(TicketDTO ticket) {
        Request r = new Request();
        r.setType(RequestType.SELL_TICKET);
        r.setTicket(ticket);
        return r;
    }

    public static Request createGetTicketsRequest(ShowDTO show) {
        Request r = new Request();
        r.setType(RequestType.GET_TICKETS_FOR_SHOW);
        r.setShow(show);
        return r;
    }

    public static Response createOkResponse() {
        Response r = new Response();
        r.setType(ResponseType.OK);
        return r;
    }

    public static Response createErrorResponse(String error) {
        Response r = new Response();
        r.setType(ResponseType.ERROR);
        r.setError(error);
        return r;
    }

    public static Response createLoginSuccessResponse(EmployeeDTO dto) {
        Response r = new Response();
        r.setType(ResponseType.LOGIN_SUCCESS);
        r.setEmployee(dto);
        return r;
    }

    public static Response createShowsListResponse(List<ShowDTO> shows) {
        Response r = new Response();
        r.setType(ResponseType.SHOWS_LIST);
        r.setShows(shows);
        return r;
    }

    public static Response createTicketsListResponse(List<TicketDTO> tickets) {
        Response r = new Response();
        r.setType(ResponseType.TICKETS_LIST);
        r.setTickets(tickets);
        return r;
    }

    public static Response createShowUpdatedResponse(ShowDTO show) {
        Response r = new Response();
        r.setType(ResponseType.SHOW_UPDATED);
        r.setShow(show);
        return r;
    }
}