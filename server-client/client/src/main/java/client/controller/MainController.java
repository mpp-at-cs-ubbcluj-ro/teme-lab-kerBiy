package client.controller;

import client.MainApp;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Employee;
import models.Show;
import services.IObserver;
import services.ServiceException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MainController extends GenericController implements IObserver {

    @FXML
    private Label welcomeLabel;

    @FXML
    private DatePicker searchDatePicker;

    @FXML
    private TableView<Show> showTable;

    @FXML
    private TableColumn<Show, String> artistCol;

    @FXML
    private TableColumn<Show, String> locationCol;

    @FXML
    private TableColumn<Show, String> dateCol;

    @FXML
    private TableColumn<Show, Integer> availableSeatsCol;

    @FXML
    private TableColumn<Show, Integer> soldSeatsCol;

    private Employee loggedEmployee;
    private ObservableList<Show> masterData = FXCollections.observableArrayList();

    @Override
    public void setSomething(Optional<Object> param) {
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artist"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        dateCol.setCellValueFactory(cell -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            return new ReadOnlyStringWrapper(sdf.format(cell.getValue().getDate()));
        });
        availableSeatsCol.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getTotalSeats() - cell.getValue().getSoldSeats()));
        soldSeatsCol.setCellValueFactory(new PropertyValueFactory<>("soldSeats"));

        param.ifPresent(p -> {
            if (p instanceof Employee e) {
                loggedEmployee = e;
                welcomeLabel.setText(e.getUsername());
                loadShows();
            }
        });
    }

    private void loadShows() {
        try {
            List<Show> shows = service.getAllShows();
            masterData.setAll(shows);
            showTable.setItems(masterData);
        } catch (ServiceException e) {
            showError("Eroare la încărcarea spectacolelor: " + e.getMessage());
        }
    }

    @FXML
    private void handleSearch() {
        LocalDate selectedDate = searchDatePicker.getValue();
        if (selectedDate != null) {
            List<Show> filtered = masterData.stream()
                    .filter(show -> toLocalDate(show.getDate()).equals(selectedDate))
                    .toList();
            showTable.setItems(FXCollections.observableArrayList(filtered));
        }
    }

    @FXML
    private void handleClearSearch() {
        searchDatePicker.setValue(null);
        showTable.setItems(masterData);
    }

    @FXML
    private void handleSellTicket() {
        Show selected = showTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Show Selected", "Please select a show to sell tickets for.");
            return;
        }

        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Sell Ticket");
        nameDialog.setHeaderText("Enter buyer name:");
        Optional<String> buyerResult = nameDialog.showAndWait();
        if (buyerResult.isEmpty()) return;

        TextInputDialog seatsDialog = new TextInputDialog();
        seatsDialog.setTitle("Sell Ticket");
        seatsDialog.setHeaderText("Enter number of seats:");
        Optional<String> seatsResult = seatsDialog.showAndWait();
        if (seatsResult.isEmpty()) return;

        try {
            int seats = Integer.parseInt(seatsResult.get());
            service.sellTicket(buyerResult.get(), seats, selected);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Ticket sold successfully.");
            loadShows();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number of seats.");
        } catch (ServiceException e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    public void onLogoutClick() {
        try {
            service.logout(loggedEmployee, this);
            MainApp.showLoginView();
        } catch (ServiceException | IOException e) {
            showError("Eroare la logout: " + e.getMessage());
        }
    }

    @Override
    public void showUpdated(Show updatedShow) {
        Platform.runLater(this::loadShows);
    }

    private void showError(String msg) {
        showAlert(Alert.AlertType.ERROR, "Eroare", msg);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private LocalDate toLocalDate(Date date) {
        return new java.sql.Date(date.getTime()).toLocalDate();
    }
}