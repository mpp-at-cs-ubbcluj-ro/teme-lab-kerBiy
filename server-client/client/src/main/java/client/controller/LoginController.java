package client.controller;

import client.ClientObserver;
import client.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import models.Employee;
import services.ServiceException;

public class LoginController extends GenericController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void onLoginClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Username și parola nu pot fi goale.");
            return;
        }

        try {
            ClientObserver observer = new ClientObserver();
            Employee emp = service.login(username, password, observer);
            MainApp.showMainView(emp, observer);
        } catch (ServiceException e) {
            showAlert("Autentificare eșuată: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Eroare");
        alert.setContentText(message);
        alert.showAndWait();
    }
}