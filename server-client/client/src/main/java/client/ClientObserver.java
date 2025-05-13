package client;

import client.controller.MainController;
import javafx.application.Platform;
import models.Show;
import services.IObserver;

public class ClientObserver implements IObserver {

    private MainController mainController;

    public void setMainController(MainController controller) {
        this.mainController = controller;
    }

    @Override
    public void showUpdated(Show show) {
        if (mainController != null) {
            Platform.runLater(() -> mainController.showUpdated(show));
        } else {
            System.out.println("Received update but no controller is bound.");
        }
    }
}