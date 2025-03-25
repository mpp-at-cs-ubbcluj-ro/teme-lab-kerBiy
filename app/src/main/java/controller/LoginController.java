package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Employee;
import app.MainApp;

import java.util.Optional;

public class LoginController extends GenericController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    @FXML
    public void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Employee employee = service.login(username, password);
        if (employee != null) {
            MainApp.showMainView(employee);
        } else {
            errorLabel.setText("Invalid username or password");
        }
    }

    @Override
    public void setSomething(Optional<Object> parameter) {
    }
}
