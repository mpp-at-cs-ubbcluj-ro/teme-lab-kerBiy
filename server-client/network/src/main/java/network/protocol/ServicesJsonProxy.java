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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class ServicesJsonProxy implements IServices {
    private final String host;
    private final int port;
    private IObserver clientObserver;
    private Socket connection;
    private BufferedReader input;
    private PrintWriter output;
    private Gson gson;
    private BlockingQueue<Response> responses;
    private volatile boolean finished;

    public ServicesJsonProxy(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<>();
//        initializeConnection();
    }

    private void initializeConnection() throws IOException {
        try {
            gson = new Gson();
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (IOException e) {
            System.err.println("Could not connect to " + host + ":" + port);
        }
    }

    private void startReader() {
        Thread t = new Thread(new ReaderThread());
        t.setDaemon(true);
        t.start();
    }

    private void sendRequest(Request request) {
        String reqLine = gson.toJson(request);
        output.println(reqLine);
        output.flush();
    }

    private Response readResponse() throws ServiceException {
        try {
            return responses.take();
        } catch (InterruptedException e) {
            throw new ServiceException("Error reading response", e);
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public Employee login(String username, String password, IObserver client) throws ServiceException, IOException {
        initializeConnection();
        this.clientObserver = client;

        Request req = JsonProtocolUtils.createLoginRequest(new EmployeeDTO(username, password));
        sendRequest(req);

        Response response = readResponse();

        switch (response.getType()) {
            case LOGIN_SUCCESS -> {
                return DTOUtils.dtoToEmployee(response.getEmployee());
            }
            case ERROR -> {
                System.out.println(">>> Login failed: " + response.getError());
                throw new ServiceException(response.getError());
            }
            default -> {
                closeConnection();
                throw new ServiceException("Unexpected response type: " + response.getType());
            }
        }
    }

    @Override
    public void logout(Employee employee, IObserver client) throws ServiceException {
        Request req = JsonProtocolUtils.createLogoutRequest(DTOUtils.employeeToDTO(employee));
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.getType() != ResponseType.OK) {
            throw new ServiceException(response.getError());
        }
    }

    @Override
    public List<Show> getAllShows() throws ServiceException {
        sendRequest(JsonProtocolUtils.createSimpleRequest(RequestType.GET_ALL_SHOWS));
        Response response = readResponse();
        if (response.getType() == ResponseType.SHOWS_LIST) {
            return response.getShows().stream().map(DTOUtils::dtoToShow).collect(Collectors.toList());
        } else {
            throw new ServiceException(response.getError());
        }
    }

    @Override
    public void sellTicket(String buyerName, int numberOfSeats, Show show) throws ServiceException {
        Ticket ticket = new Ticket(buyerName, numberOfSeats, show.getId());
        sendRequest(JsonProtocolUtils.createSellTicketRequest(DTOUtils.ticketToDTO(ticket)));
        Response response = readResponse();
        if (response.getType() != ResponseType.OK) {
            throw new ServiceException(response.getError());
        }
    }

    @Override
    public List<Ticket> getTicketsForShow(Show show) throws ServiceException {
        sendRequest(JsonProtocolUtils.createGetTicketsRequest(DTOUtils.showToDTO(show)));
        Response response = readResponse();
        if (response.getType() == ResponseType.TICKETS_LIST) {
            return response.getTickets().stream().map(DTOUtils::dtoToTicket).collect(Collectors.toList());
        } else {
            throw new ServiceException(response.getError());
        }
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    String line = input.readLine();
                    if (line != null) {
                        Response response = gson.fromJson(line, Response.class);
                        if (response.getType() == ResponseType.SHOW_UPDATED) {
                            if (clientObserver != null) {
                                clientObserver.showUpdated(DTOUtils.dtoToShow(response.getShow()));
                            }
                        } else {
                            responses.put(response);
                        }
                    }
                } catch (Exception e) {
                    System.err.println(">>> [Proxy] ReaderThread error: " + e.getMessage());
                }
            }
        }
    }
}