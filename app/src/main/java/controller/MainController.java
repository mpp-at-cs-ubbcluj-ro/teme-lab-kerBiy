package controller;

import app.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Employee;
import model.Show;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MainController extends GenericController {
    @FXML
    private Label employeeLabel;
    @FXML
    private TableView<Show> showTable;
    @FXML
    private TableColumn<Show, String> artistColumn;
    @FXML
    private TableColumn<Show, String> dateColumn;
    @FXML
    private TableColumn<Show, String> locationColumn;
    @FXML
    private TableColumn<Show, Integer> availableSeatsColumn;
    @FXML
    private TableColumn<Show, Integer> soldSeatsColumn;
    @FXML
    private DatePicker searchDatePicker;

    private Employee loggedInEmployee;
    private ObservableList<Show> masterData;

    @Override
    public void setSomething(Optional<Object> parameter) {
        if (parameter.isPresent() && parameter.get() instanceof Employee) {
            loggedInEmployee = (Employee) parameter.get();
        }
        employeeLabel.setText(loggedInEmployee.getUsername());
        loadShowData();
    }

    private void loadShowData() {
        List<Show> allShows = service.getAllShows();
        masterData = FXCollections.observableArrayList(allShows);

        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        availableSeatsColumn.setCellValueFactory(cellData -> javafx.beans.binding.Bindings.createIntegerBinding(
                () -> cellData.getValue().getTotalSeats() - cellData.getValue().getSoldSeats()).asObject());
        soldSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("soldSeats"));

        showTable.setItems(masterData);
    }

    @FXML
    private void handleLogout() {
        MainApp.showLogInView();
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
        showTable.setItems(masterData);
        searchDatePicker.setValue(null);
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
            boolean success = service.sellTicket(buyerResult.get(), seats, selected);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Ticket sold successfully.");
                loadShowData();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", "Not enough seats available.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number of seats.");
        }
    }

    private LocalDate toLocalDate(Date date) {
        return ((java.sql.Date) date).toLocalDate();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
