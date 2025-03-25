package app;

import controller.GenericController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Employee;
import repository.EmployeeRepository;
import repository.ShowRepository;
import repository.TicketRepository;
import service.EmployeeService;
import service.Service;
import service.ShowService;
import service.TicketService;
import utils.Jdbc;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class MainApp extends Application {
    private static Service service;
    private static Stage stage;

    @Override
    public void init() {
        Properties props = new Properties();
        try {
            props.load(new FileReader("db.config"));
        } catch (IOException e) {
            throw new RuntimeException("Cannot load db.config: " + e.getMessage(), e);
        }

        Jdbc jdbc = new Jdbc(props);

        var employeeRepo = new EmployeeRepository(jdbc);
        var showRepo = new ShowRepository(jdbc);
        var ticketRepo = new TicketRepository(jdbc);

        var employeeService = new EmployeeService(employeeRepo);
        var showService = new ShowService(showRepo);
        var ticketService = new TicketService(ticketRepo, showRepo);

        service = new Service(showService, ticketService, employeeService);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        showLogInView();
        stage.show();
    }

    public static void showLogInView() {
        stage.setScene(createScene("login-view.fxml", Optional.empty()));
        stage.setTitle("Festival - Login");
    }

    public static void showMainView(Employee employee) {
        stage.setScene(createScene("main-view.fxml", Optional.of(employee)));
        stage.setTitle("Festival - Dashboard");
    }

    private static Scene createScene(String fxml, Optional<Object> parameter) {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource(fxml));

        try {
            Scene scene = new Scene(loader.load());

            GenericController controller = loader.getController();
            controller.setService(service);
            controller.setSomething(parameter);

            return scene;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: " + fxml, e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}