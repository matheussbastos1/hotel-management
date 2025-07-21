package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.dto.PaymentReportDTO;
import com.example.hotelmanagement.models.Payment;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl;
import com.example.hotelmanagement.util.DataPersistence;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FinancialReportController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField guestNameField;
    @FXML private TableView<PaymentReportDTO> paymentsTableView;
    @FXML private Label totalAmountLabel;

    @FXML private TableColumn<PaymentReportDTO, Integer> paymentIdColumn;
    @FXML private TableColumn<PaymentReportDTO, String> guestNameColumn;
    @FXML private TableColumn<PaymentReportDTO, LocalDateTime> paymentDateColumn;
    @FXML private TableColumn<PaymentReportDTO, BigDecimal> amountColumn;
    @FXML private TableColumn<PaymentReportDTO, String> methodColumn;
    @FXML private TableColumn<PaymentReportDTO, String> statusColumn;
    @FXML private TableColumn<PaymentReportDTO, Integer> roomColumn;

    private final ReservationRepository reservationRepository = ReservationRepositoryImpl.getInstance();
    private final ObservableList<PaymentReportDTO> allPayments = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadPaymentsFromDatabase();
        paymentsTableView.setItems(allPayments);
        updateTotalAmount();
    }

    private void setupTableColumns() {
        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        guestNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        methodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));

        // Formatação da coluna de valor
        amountColumn.setCellFactory(tc -> new TableCell<PaymentReportDTO, BigDecimal>() {
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

        // Formatação da coluna de data
        paymentDateColumn.setCellFactory(tc -> new TableCell<PaymentReportDTO, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime dateTime, boolean empty) {
                super.updateItem(dateTime, empty);
                if (empty || dateTime == null) {
                    setText(null);
                } else {
                    setText(dateTime.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }
            }
        });
    }

    private void loadPaymentsFromDatabase() {
        allPayments.clear();
        try {
            List<Payment> payments = DataPersistence.loadFromFile("payments");
            if (payments == null) {
                payments = new ArrayList<>();
            }

            List<PaymentReportDTO> paymentDTOs = payments.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            allPayments.addAll(paymentDTOs);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar pagamentos: " + e.getMessage());
        }
    }

    private PaymentReportDTO convertToDTO(Payment payment) {
        PaymentReportDTO dto = new PaymentReportDTO();
        dto.setPaymentId(payment.getPaymentId());
        dto.setInvoiceId(payment.getInvoiceId());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod() != null ? payment.getPaymentMethod().toString() : "N/A");
        dto.setPaymentStatus(payment.getPaymentStatus() != null ? payment.getPaymentStatus().toString() : "N/A");

        // Usar as informações diretas do pagamento
        dto.setGuestName(payment.getGuestName() != null ? payment.getGuestName() : "Hóspede não identificado");
        dto.setRoomNumber(payment.getRoomNumber() != 0 ? payment.getRoomNumber() : 0);

        return dto;
    }

    private String findGuestNameFromPayment(Payment payment) {
        try {
            // Como não temos referência direta, buscamos por data de pagamento próxima ao checkout
            LocalDate paymentDate = payment.getPaymentDate().toLocalDate();

            List<Reservation> reservations = reservationRepository.getAllReservations();
            return reservations.stream()
                    .filter(r -> r.getStatus() == com.example.hotelmanagement.models.ReservationStatus.CHECKED_OUT)
                    .filter(r -> r.getUpdatedAt() != null &&
                            r.getUpdatedAt().toLocalDate().equals(paymentDate))
                    .map(r -> r.getPrincipalGuest() != null ? r.getPrincipalGuest().getName() : "Hóspede não identificado")
                    .findFirst()
                    .orElse("Hóspede não encontrado");
        } catch (Exception e) {
            return "Erro ao buscar hóspede";
        }
    }

    private Integer findRoomNumberFromPayment(Payment payment) {
        try {
            LocalDate paymentDate = payment.getPaymentDate().toLocalDate();

            List<Reservation> reservations = reservationRepository.getAllReservations();
            return reservations.stream()
                    .filter(r -> r.getStatus() == com.example.hotelmanagement.models.ReservationStatus.CHECKED_OUT)
                    .filter(r -> r.getUpdatedAt() != null &&
                            r.getUpdatedAt().toLocalDate().equals(paymentDate))
                    .map(r -> r.getRoom() != null ? r.getRoom().getRoomNumber() : null)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    @FXML
    private void handleFilterByDate(ActionEvent event) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            paymentsTableView.setItems(allPayments);
            updateTotalAmount();
            return;
        }

        ObservableList<PaymentReportDTO> filteredList = allPayments.stream()
                .filter(p -> {
                    LocalDate paymentDate = p.getPaymentDate().toLocalDate();
                    return !paymentDate.isBefore(startDate) && !paymentDate.isAfter(endDate);
                })
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        paymentsTableView.setItems(filteredList);
        updateTotalAmount();
    }

    @FXML
    private void handleSearchByGuest(ActionEvent event) {
        String guestName = guestNameField.getText();
        if (guestName == null || guestName.trim().isEmpty()) {
            paymentsTableView.setItems(allPayments);
            updateTotalAmount();
            return;
        }

        ObservableList<PaymentReportDTO> filteredList = allPayments.stream()
                .filter(p -> p.getGuestName().toLowerCase().contains(guestName.toLowerCase()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        paymentsTableView.setItems(filteredList);
        updateTotalAmount();
    }

    @FXML
    private void handleRefresh() {
        loadPaymentsFromDatabase();
        paymentsTableView.setItems(allPayments);
        updateTotalAmount();
        showAlert(Alert.AlertType.INFORMATION, "Atualizado", "Relatório atualizado com sucesso!");
    }

    private void updateTotalAmount() {
        BigDecimal total = paymentsTableView.getItems().stream()
                .map(PaymentReportDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalAmountLabel != null) {
            totalAmountLabel.setText("Total: R$ " + String.format("%.2f", total.doubleValue()));
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
}