package Controller;

import Domain.Bilet;
import Domain.Zbor;
import Service.Service;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationController {
    @FXML
    private Label messageLabel;
    @FXML
    private TableView<Zbor> flightsTable;
    @FXML
    private TableColumn<Zbor, String> destinationColumn;
    @FXML
    private TableColumn<Zbor, String> searchedDestinationColumn;
    @FXML
    private TableColumn<Zbor, LocalDateTime> dateColumn;
    @FXML
    private TableColumn<Zbor, LocalDateTime> timeColumn;
    @FXML
    private TableColumn<Zbor, LocalDateTime> searchedTimeColumn;
    @FXML
    private TableColumn<Zbor, Integer> availableSeatsColumn;
    @FXML
    private TableColumn<Zbor, Integer> searchAvailableSeatsColumn;
    @FXML
    private TableColumn<Zbor, String> airportColumn;
    @FXML
    private TableView<Zbor> allFlightsTable;
    @FXML
    private TextField destinationField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField nameField;
    @FXML
    private TextField touristsField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField destinationBuyField;
    @FXML
    private DatePicker dateBuyField;
    @FXML
    private TextField timeBuyField;


    private Service service;

    public void initialize(Service service) {
        this.service = service;
        initializeColumns();
        populateTable();
        allFlightsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showFlightDetails(newSelection);
            }
        });
    }

    private void initializeColumns() {
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dataPlecare"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("oraPlecare"));
        airportColumn.setCellValueFactory(new PropertyValueFactory<>("aeroport"));
        availableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("locuriDisponibile"));
    }

    private void populateTable() {
        List<Zbor> flights = service.getAllZboruri();
        allFlightsTable.getItems().setAll(flights);
    }

    @FXML
    private void showFlightDetails(Zbor zbor) {
        destinationField.setText(zbor.getDestinatie());
        datePicker.setValue(zbor.getDataPlecare());
    }

    private void initializeSearchColumns() {
        searchedDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destinatie"));
        searchedTimeColumn.setCellValueFactory(new PropertyValueFactory<>("oraPlecare"));
        searchAvailableSeatsColumn.setCellValueFactory(new PropertyValueFactory<>("locuriDisponibile"));
    }

    @FXML
    private void searchAndDisplayFlightsDetails() {
        String destination = destinationField.getText();
        LocalDate date = datePicker.getValue();

        if (destination.isEmpty() || date == null) {
            showAlert("Error", "Invalid input", "Please provide a destination and date");
            return;
        }

        try {
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = date.format(outputFormatter);

            // Apelați metoda din service pentru a căuta zborurile după destinație și dată
            List<Zbor> flights = service.findByDestinationAndDate(destination, formattedDate);

            // Actualizați tabelul cu rezultatul căutării
            flightsTable.getItems().setAll(flights);

            initializeSearchColumns();
        } catch (Exception e) {
            showAlert("Error", "Search Error", "An error occurred while searching for flights: " + e.getMessage());
        }
    }


    private void updateFlightInTable(Zbor updatedZbor) {
        // Obțineți lista de zboruri din tabel
        ObservableList<Zbor> flightsList = allFlightsTable.getItems();

        // Găsiți indexul zborului actualizat în lista de zboruri
        int index = -1;
        for (int i = 0; i < flightsList.size(); i++) {
            if (flightsList.get(i).getId() == updatedZbor.getId()) {
                index = i;
                break;
            }
        }

        // Dacă zborul actualizat există în tabel
        if (index != -1) {
            // Eliminați zborul din tabel
            flightsList.remove(index);

            // Adăugați zborul actualizat în tabel
            flightsList.add(index, updatedZbor);
        }
    }


    @FXML
    private void buyTicket() {
        String destination = destinationBuyField.getText();
        LocalDate date = dateBuyField.getValue();
        String time = timeBuyField.getText();
        String name = nameField.getText();
        String address = addressField.getText();
        String tourists = touristsField.getText();
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = date.format(outputFormatter);

        int numarLocuri;
        List<String> turisti;
        if (tourists.trim().isEmpty()) {
            numarLocuri = 1;
            turisti = new ArrayList<>();
        } else {
            numarLocuri = tourists.split(", ").length + 1;
            turisti = Arrays.asList(tourists.split(", "));
        }

        Zbor zbor = service.findFlightByDestinationDateAndTime(destination, formattedDate, time);

        if (zbor == null) {
            showAlert("Error", "Flight not found", "No flight found for the specified destination, date, and time.");
            return;
        }

        boolean purchased = service.sellFlightTicket(zbor.getId(), numarLocuri);

        if (purchased) {
            // Salvăm biletul în baza de date
            Bilet bilet = new Bilet(zbor, name, address, turisti);
            bilet.setNumarLocuri(numarLocuri);
            service.addBilet(bilet);

            Zbor zborToUpdate = service.findFlightById(zbor.getId());

            // Actualizați numărul de locuri disponibile
            zborToUpdate.setLocuriDisponibile(zborToUpdate.getLocuriDisponibile() - numarLocuri);

            updateFlightInTable(zborToUpdate);
            showAlert("Success", "Ticket purchased", "Ticket purchased successfully.");
        } else {
            showAlert("Error", "Ticket not purchased", "The ticket could not be purchased. Please try again later.");
        }
    }

    @FXML
    private void logoutButtonClicked() throws IOException {
        Stage currentStage = (Stage) messageLabel.getScene().getWindow();
        currentStage.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
        Stage loginStage = new Stage();
        loginStage.setScene(new Scene(fxmlLoader.load()));
        LoginController loginController = fxmlLoader.getController();
        loginController.setService(service);
        loginStage.show();
    }


    private void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
