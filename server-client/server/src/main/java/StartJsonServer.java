import network.utils.JsonConcurrentServer;
import network.utils.ServerException;
import persistence.IEmployeeRepository;
import persistence.IShowRepository;
import persistence.ITicketRepository;
import persistence.repositories.EmployeeRepositoryJdbc;
import persistence.repositories.JdbcUtils;
import persistence.repositories.ShowRepositoryJdbc;
import persistence.repositories.TicketRepositoryJdbc;
import server.ServiceImpl;
import services.IServices;

import java.io.IOException;
import java.util.Properties;

public class StartJsonServer {
    private static int defaultPort = 55556;

    public static void main(String[] args) {
        Properties props = new Properties();
        try {
            props.load(StartJsonServer.class.getResourceAsStream("/db.config"));
            System.out.println("DB config loaded.");
        } catch (IOException e) {
            System.err.println("Cannot load db.config: " + e.getMessage());
            return;
        }

        JdbcUtils jdbc = new JdbcUtils(props);
        IEmployeeRepository empRepo = new EmployeeRepositoryJdbc(jdbc);
        IShowRepository showRepo = new ShowRepositoryJdbc(jdbc);
        ITicketRepository ticketRepo = new TicketRepositoryJdbc(jdbc);

        IServices service = new ServiceImpl(empRepo, showRepo, ticketRepo);

        int port = defaultPort;
        JsonConcurrentServer server = new JsonConcurrentServer(port, service);

        try {
            System.out.println("Starting server on port " + port + "...");
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }
    }
}