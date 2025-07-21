package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.dto.StayReportDTO;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.ReservationStatus;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class StayReportController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField guestNameField;
    @FXML private TableView<StayReportDTO> staysTableView;
    @FXML private TableColumn<StayReportDTO, Long> reservationIdColumn;
    @FXML private TableColumn<StayReportDTO, String> guestNameColumn;
    @FXML private TableColumn<StayReportDTO, Integer> roomNumberColumn;
    @FXML private TableColumn<StayReportDTO, String> checkInDateColumn;
    @FXML private TableColumn<StayReportDTO, String> checkOutDateColumn;
    @FXML private TableColumn<StayReportDTO, Long> nightsColumn;
    @FXML private TableColumn<StayReportDTO, String> statusColumn;
    @FXML private Label totalStaysLabel;

    private final ReservationRepository reservationRepository = ReservationRepositoryImpl.getInstance();
    private ObservableList<StayReportDTO> staysList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadAllStays();
    }

    private void setupTableColumns() {
        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        guestNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        checkInDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        checkOutDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        nightsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfNights"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        staysTableView.setItems(staysList);
    }

    private void loadAllStays() {
        List<Reservation> allReservations = reservationRepository.getAllReservations();
        updateTableWithReservations(allReservations);
    }

    private void updateTableWithReservations(List<Reservation> reservations) {
        staysList.clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Reservation reservation : reservations) {
            if (reservation.getStatus() != ReservationStatus.CANCELLED) {
                long nights = ChronoUnit.DAYS.between(
                        reservation.getCheckInDate(),
                        reservation.getCheckOutDate()
                );
                if (nights == 0) nights = 1;

                StayReportDTO dto = new StayReportDTO(
                        reservation.getReservationId(),
                        reservation.getPrincipalGuest().getName(),
                        reservation.getRoom().getRoomNumber(),
                        reservation.getCheckInDate().format(formatter),
                        reservation.getCheckOutDate().format(formatter),
                        nights,
                        reservation.getStatus().name()
                );
                staysList.add(dto);
            }
        }

        totalStaysLabel.setText("Total de Estadias: " + staysList.size());
    }

    @FXML
    private void handleFilterByDate() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Por favor, selecione ambas as datas para filtrar.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showAlert("A data de início deve ser anterior à data de fim.");
            return;
        }

        List<Reservation> filteredReservations = reservationRepository.getAllReservations()
                .stream()
                .filter(r -> !r.getCheckInDate().isBefore(startDate) &&
                        !r.getCheckInDate().isAfter(endDate))
                .collect(Collectors.toList());

        updateTableWithReservations(filteredReservations);
    }

    @FXML
    private void handleSearchByGuest() {
        String guestName = guestNameField.getText().trim();

        if (guestName.isEmpty()) {
            loadAllStays();
            return;
        }

        List<Reservation> filteredReservations = reservationRepository.getAllReservations()
                .stream()
                .filter(r -> r.getPrincipalGuest().getName()
                        .toLowerCase().contains(guestName.toLowerCase()))
                .collect(Collectors.toList());

        updateTableWithReservations(filteredReservations);
    }

    @FXML
    private void handleRefresh() {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        guestNameField.clear();
        loadAllStays();
    }

    @FXML
    private void handleVoltar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ReportsForm.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Central de Relatórios");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}