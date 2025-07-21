package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.dto.ReservationDetailsDTO;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.ReservationStatus;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl;
import com.example.hotelmanagement.util.ReportExporter;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ReservationReportController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TableView<ReservationDetailsDTO> reservationsTableView;

    @FXML private TableColumn<ReservationDetailsDTO, Long> idColumn;
    @FXML private TableColumn<ReservationDetailsDTO, String> guestNameColumn;
    @FXML private TableColumn<ReservationDetailsDTO, Integer> roomNumberColumn;
    @FXML private TableColumn<ReservationDetailsDTO, LocalDate> checkInColumn;
    @FXML private TableColumn<ReservationDetailsDTO, LocalDate> checkOutColumn;
    @FXML private TableColumn<ReservationDetailsDTO, String> statusColumn;
    @FXML private TableColumn<ReservationDetailsDTO, BigDecimal> totalAmountColumn;
    @FXML private TableColumn<ReservationDetailsDTO, String> paymentStatusColumn;

    private final ReservationRepository reservationRepository = new ReservationRepositoryImpl();
    private final ObservableList<ReservationDetailsDTO> allReservations = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadReservations();
        reservationsTableView.setItems(allReservations);
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        guestNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        checkInColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        checkOutColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("reservationStatus"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        paymentStatusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        // Formatação da coluna de valor
        totalAmountColumn.setCellFactory(tc -> new TableCell<ReservationDetailsDTO, BigDecimal>() {
            @Override
            protected void updateItem(BigDecimal price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                } else {
                    setText(String.format("R$ %.2f", price.doubleValue()));
                }
            }
        });
    }

    private void loadReservations() {
        allReservations.clear();
        try {
            List<Reservation> reservations = reservationRepository.getAllReservations();
            List<ReservationDetailsDTO> reservationDTOs = reservations.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            allReservations.addAll(reservationDTOs);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar reservas: " + e.getMessage());
        }
    }

    private ReservationDetailsDTO convertToDTO(Reservation reservation) {
        ReservationDetailsDTO dto = new ReservationDetailsDTO();
        dto.setReservationId(reservation.getReservationId());
        dto.setCheckInDate(reservation.getCheckInDate());
        dto.setCheckOutDate(reservation.getCheckOutDate());
        dto.setReservationStatus(reservation.getStatus());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());

        // Dados do quarto
        if (reservation.getRoom() != null) {
            dto.setRoomNumber(reservation.getRoom().getRoomNumber());
            dto.setRoomType(reservation.getRoom().getRoomType());
            dto.setBedType(reservation.getRoom().getBedType());
            dto.setRoomStatus(reservation.getRoom().getStatus());
            dto.setRoomPrice(reservation.getRoom().getPrice());

            // Cálculo do valor total
            long nights = java.time.temporal.ChronoUnit.DAYS.between(
                    reservation.getCheckInDate(), reservation.getCheckOutDate());
            if (nights == 0) nights = 1;
            double totalAmount = nights * reservation.getRoom().getPrice();
            dto.setTotalAmount(BigDecimal.valueOf(totalAmount));
        }

        // Dados do hóspede
        if (reservation.getPrincipalGuest() != null) {
            dto.setGuestId(reservation.getPrincipalGuest().getId());
            dto.setGuestName(reservation.getPrincipalGuest().getName());
            dto.setGuestEmail(reservation.getPrincipalGuest().getEmail());
            dto.setGuestPhone(reservation.getPrincipalGuest().getPhone());
        }

        // Status de pagamento baseado no status da reserva
        if (reservation.getStatus() == ReservationStatus.CHECKED_OUT) {
            dto.setIsPaid(true);
            dto.setPaymentStatus(com.example.hotelmanagement.models.PaymentStatus.COMPLETED);
            dto.setPaymentMethod("Pago no Checkout");
        } else if (reservation.getStatus() == ReservationStatus.CHECKED_IN) {
            dto.setIsPaid(false);
            dto.setPaymentStatus(com.example.hotelmanagement.models.PaymentStatus.PENDING);
            dto.setPaymentMethod("Pendente");
        } else {
            dto.setIsPaid(false);
            dto.setPaymentStatus(com.example.hotelmanagement.models.PaymentStatus.PENDING);
            dto.setPaymentMethod("Pendente");
        }

        return dto;
    }

    @FXML
    private void handleFilter() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            reservationsTableView.setItems(allReservations);
            return;
        }

        ObservableList<ReservationDetailsDTO> filteredList = allReservations.stream()
                .filter(r -> !r.getCheckInDate().isBefore(startDate) && !r.getCheckInDate().isAfter(endDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        reservationsTableView.setItems(filteredList);
    }

    @FXML
    private void handleExportCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório CSV");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        fileChooser.setInitialFileName("relatorio_reservas_" + LocalDate.now() + ".csv");

        File file = fileChooser.showSaveDialog(reservationsTableView.getScene().getWindow());
        if (file != null) {
            try {
                List<ReservationDetailsDTO> reservationsToExport = reservationsTableView.getItems()
                        .stream()
                        .collect(Collectors.toList());

                ReportExporter.exportToCSV(reservationsToExport, file.getAbsolutePath());

                showAlert(Alert.AlertType.INFORMATION, "Sucesso",
                        "Relatório CSV exportado com sucesso para: " + file.getAbsolutePath());

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro",
                        "Erro ao exportar CSV: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleExportPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório PDF");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        fileChooser.setInitialFileName("relatorio_reservas_" + LocalDate.now() + ".pdf");

        File file = fileChooser.showSaveDialog(reservationsTableView.getScene().getWindow());
        if (file != null) {
            try {
                List<ReservationDetailsDTO> reservationsToExport = reservationsTableView.getItems()
                        .stream()
                        .collect(Collectors.toList());

                ReportExporter.exportToPDF(reservationsToExport, file.getAbsolutePath());

                showAlert(Alert.AlertType.INFORMATION, "Sucesso",
                        "Relatório PDF exportado com sucesso para: " + file.getAbsolutePath());

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro",
                        "Erro ao exportar PDF: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleVoltar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ReportsForm.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Central de Relatórios");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleRefresh() {
        loadReservations();
        showAlert(Alert.AlertType.INFORMATION, "Atualizado", "Relatório atualizado com sucesso!");
    }
}