package client;

import client.controller.GenericController;
import client.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Employee;
import network.protocol.ServicesJsonProxy;
import services.IObserver;
import services.IServices;

import java.io.IOException;
import java.util.Optional;

public class MainApp extends Application {
    private static IServices service;
    private static Stage stage;

    private static final String HOST = "localhost";
    private static final int PORT = 55556;

    @Override
    public void init() throws IOException {
        service = new ServicesJsonProxy(HOST, PORT);
    }

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        showLoginView();
        stage.show();
    }

    public static void showLoginView() {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("login-view.fxml"));

        try {
            Scene scene = new Scene(loader.load());
            GenericController controller = loader.getController();

            controller.setService(service);

            stage.setScene(scene);
            stage.setTitle("Festival - Login");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: login-view.fxml", e);
        }
    }

    public static void showMainView(Employee employee, IObserver observer) {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemResource("main-view.fxml"));
        try {
            Scene scene = new Scene(loader.load());
            GenericController controller = loader.getController();

            controller.setService(service);
            controller.setSomething(Optional.of(employee));

            // Setează observer-ul ca să poată fi updatat
            MainController mainCtrl = (MainController) controller;
            if (observer instanceof ClientObserver obs) {
                obs.setMainController(mainCtrl); // Actualizează referința
            }

            stage.setScene(scene);
            stage.setTitle("Festival - Dashboard");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: main-view.fxml", e);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}