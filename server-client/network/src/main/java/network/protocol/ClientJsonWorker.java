package network.protocol;

import com.google.gson.Gson;
import models.Employee;
import models.Show;
import models.Ticket;
import network.dto.DTOUtils;
import network.dto.EmployeeDTO;
import network.dto.ShowDTO;
import network.dto.TicketDTO;
import services.IObserver;
import services.IServices;
import services.ServiceException;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class ClientJsonWorker implements Runnable, IObserver {
    private final IServices server;
    private final Socket connection;
    private final Gson gson = new Gson();
    private BufferedReader input;
    private PrintWriter output;
    private volatile boolean connected;

    private Employee loggedEmployee;

    public ClientJsonWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;

        try {
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            output = new PrintWriter(connection.getOutputStream());
            connected = true;
        } catch (IOException e) {
            throw new RuntimeException("Cannot open connection streams", e);
        }
    }

    @Override
    public void run() {
        System.out.println("Worker started for client...");
        while (connected) {
            try {
                String requestLine = input.readLine();
                if (requestLine != null && requestLine.startsWith("\uFEFF")) {
                    requestLine = requestLine.substring(1);
                }
                if (requestLine == null) {
                    System.out.println(">>> Client disconnected (null request).");
                    connected = false;
                    break;
                }

                System.out.println(">>> [Raw request] " + requestLine);
                Request request = gson.fromJson(requestLine, Request.class);
                Response response = handleRequest(request);

                if (response != null) {
                    sendResponse(response);
                }

            } catch (IOException e) {
                System.err.println("Worker error: " + e.getMessage());
                connected = false;
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
        }

        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    private void sendResponse(Response response) {
        String responseLine = gson.toJson(response);
        System.out.println(">>> [Sending response] " + responseLine);
        synchronized (output) {
            output.println(responseLine);
            output.flush();
        }
    }

    private Response handleRequest(Request request) {
        System.out.println("Received request: " + request.getType());

        try {
            switch (request.getType()) {
                case LOGIN -> {
                    Employee emp = DTOUtils.dtoToEmployee(request.getEmployee());
                    loggedEmployee = server.login(emp.getUsername(), emp.getPassword(), this);
                    return JsonProtocolUtils.createLoginSuccessResponse(DTOUtils.employeeToDTO(loggedEmployee));
                }
                case LOGOUT -> {
                    server.logout(loggedEmployee, this);
                    connected = false;
                    return JsonProtocolUtils.createOkResponse();
                }
                case GET_ALL_SHOWS -> {
                    List<Show> shows = server.getAllShows();
                    List<ShowDTO> dtoList = shows.stream().map(DTOUtils::showToDTO).collect(Collectors.toList());
                    return JsonProtocolUtils.createShowsListResponse(dtoList);
                }
                case SELL_TICKET -> {
                    Ticket ticket = DTOUtils.dtoToTicket(request.getTicket());
                    Show show = server.getAllShows().stream()
                            .filter(s -> s.getId().equals(ticket.getShowId()))
                            .findFirst()
                            .orElseThrow(() -> new ServiceException("Show not found"));
                    server.sellTicket(ticket.getName(), ticket.getNumberOfSeats(), show);
                    return JsonProtocolUtils.createOkResponse();
                }
                case GET_TICKETS_FOR_SHOW -> {
                    Show show = DTOUtils.dtoToShow(request.getShow());
                    List<Ticket> tickets = server.getTicketsForShow(show);
                    List<TicketDTO> ticketDTOs = tickets.stream().map(DTOUtils::ticketToDTO).collect(Collectors.toList());
                    return JsonProtocolUtils.createTicketsListResponse(ticketDTOs);
                }
                default -> {
                    return JsonProtocolUtils.createErrorResponse("Unknown request type: " + request.getType());
                }
            }
        } catch (ServiceException e) {
            return JsonProtocolUtils.createErrorResponse(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void showUpdated(Show show) {
        try {
            Response response = JsonProtocolUtils.createShowUpdatedResponse(DTOUtils.showToDTO(show));
            sendResponse(response);
        } catch (Exception e) {
            System.err.println("Error sending update: " + e.getMessage());
        }
    }
}