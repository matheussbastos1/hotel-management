package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.dto.PaymentReportDTO;
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
import java.util.Objects;
import java.util.stream.Collectors;

public class FinancialReportController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField guestIdField;
    @FXML private TableView<PaymentReportDTO> paymentsTableView;

    @FXML private TableColumn<PaymentReportDTO, Integer> paymentIdColumn;
    @FXML private TableColumn<PaymentReportDTO, String> guestNameColumn;
    @FXML private TableColumn<PaymentReportDTO, LocalDateTime> paymentDateColumn;
    @FXML private TableColumn<PaymentReportDTO, BigDecimal> amountColumn;
    @FXML private TableColumn<PaymentReportDTO, String> methodColumn;
    @FXML private TableColumn<PaymentReportDTO, String> statusColumn;

    private final ObservableList<PaymentReportDTO> allPayments = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadMockData();
        paymentsTableView.setItems(allPayments);
    }

    private void setupTableColumns() {
        paymentIdColumn.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        guestNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        paymentDateColumn.setCellValueFactory(new PropertyValueFactory<>("paymentDate"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        methodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
    }

    private void loadMockData() {
        allPayments.add(new PaymentReportDTO(1, 101L, "João da Silva", LocalDateTime.of(2025, 7, 5, 10, 30), new BigDecimal("450.00"), "Cartão de Crédito", "Aprovado"));
        allPayments.add(new PaymentReportDTO(2, 102L, "Maria Oliveira", LocalDateTime.of(2025, 7, 6, 14, 0), new BigDecimal("900.50"), "Pix", "Aprovado"));
        allPayments.add(new PaymentReportDTO(3, 101L, "João da Silva", LocalDateTime.of(2025, 6, 20, 11, 0), new BigDecimal("150.00"), "Boleto", "Aprovado"));
        allPayments.add(new PaymentReportDTO(4, 103L, "Pedro Martins", LocalDateTime.of(2025, 7, 1, 18, 45), new BigDecimal("320.00"), "Cartão de Débito", "Aprovado"));
    }

    @FXML
    private void handleFilterByDate(ActionEvent event) {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            paymentsTableView.setItems(allPayments);
            return;
        }

        // --- CÓDIGO CORRIGIDO AQUI ---
        ObservableList<PaymentReportDTO> filteredList = allPayments.stream()
                .filter(p -> !p.getPaymentDate().toLocalDate().isBefore(startDate) && !p.getPaymentDate().toLocalDate().isAfter(endDate))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        paymentsTableView.setItems(filteredList);
    }

    @FXML
    private void handleSearchByGuest(ActionEvent event) {
        String guestIdText = guestIdField.getText();
        if (guestIdText == null || guestIdText.trim().isEmpty()) {
            paymentsTableView.setItems(allPayments);
            return;
        }

        try {
            long guestId = Long.parseLong(guestIdText);

            // --- CÓDIGO CORRIGIDO AQUI ---
            ObservableList<PaymentReportDTO> filteredList = allPayments.stream()
                    .filter(p -> p.getGuestId().equals(guestId))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            paymentsTableView.setItems(filteredList);
        } catch (NumberFormatException e) {
            paymentsTableView.setItems(FXCollections.observableArrayList());
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
}