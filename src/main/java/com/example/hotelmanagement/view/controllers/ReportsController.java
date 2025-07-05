package com.example.hotelmanagement.view.controllers;


import com.example.hotelmanagement.dto.ReservationDetailsDTO; // Importe seu DTO
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReportsController {

    @FXML
    private TableView<ReservationDetailsDTO> reportsTableView;
    @FXML
    private TableColumn<ReservationDetailsDTO, Long> reservationIdColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, String> guestNameColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, LocalDate> checkInDateColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, LocalDate> checkOutDateColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, ?> reservationStatusColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, Integer> roomNumberColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, ?> roomTypeColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, Double> roomPriceColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, BigDecimal> totalAmountColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, Boolean> isPaidColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, String> paymentMethodColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, String> guestEmailColumn;
    @FXML
    private TableColumn<ReservationDetailsDTO, String> guestPhoneColumn;

    @FXML
    public void initialize() {
        // "Linka" cada coluna a um atributo da classe ReservationDetailsDTO.
        // O nome em " " deve ser EXATAMENTE igual ao nome do atributo no DTO.
        reservationIdColumn.setCellValueFactory(new PropertyValueFactory<>("reservationId"));
        guestNameColumn.setCellValueFactory(new PropertyValueFactory<>("guestName"));
        checkInDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkInDate"));
        checkOutDateColumn.setCellValueFactory(new PropertyValueFactory<>("checkOutDate"));
        reservationStatusColumn.setCellValueFactory(new PropertyValueFactory<>("reservationStatus"));
        roomNumberColumn.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomTypeColumn.setCellValueFactory(new PropertyValueFactory<>("roomType"));
        roomPriceColumn.setCellValueFactory(new PropertyValueFactory<>("roomPrice"));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        isPaidColumn.setCellValueFactory(new PropertyValueFactory<>("isPaid"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        guestEmailColumn.setCellValueFactory(new PropertyValueFactory<>("guestEmail"));
        guestPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("guestPhone"));

        // Carrega os dados na tabela
        loadReportData();
    }

    private void loadReportData() {
        ObservableList<ReservationDetailsDTO> reportData = FXCollections.observableArrayList();
        com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl reservationRepository = new com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl();

        for (com.example.hotelmanagement.models.Reservation reservation : reservationRepository.getAllReservations()) {
            ReservationDetailsDTO dto = new ReservationDetailsDTO();
            dto.setReservationId(reservation.getReservationId());
            dto.setGuestName(reservation.getPrincipalGuest() != null ? reservation.getPrincipalGuest().getName() : "");
            dto.setCheckInDate(reservation.getCheckInDate());
            dto.setCheckOutDate(reservation.getCheckOutDate());
            dto.setRoomNumber(reservation.getRoom() != null ? reservation.getRoom().getRoomNumber() : null);
            dto.setRoomType(reservation.getRoom() != null ? reservation.getRoom().getRoomType() : null);
            dto.setRoomPrice(reservation.getRoom() != null ? reservation.getRoom().getPrice() : null);
            dto.setGuestEmail(reservation.getPrincipalGuest() != null ? reservation.getPrincipalGuest().getEmail() : "");
            dto.setGuestPhone(reservation.getPrincipalGuest() != null ? reservation.getPrincipalGuest().getPhone() : "");
            // Preencha outros campos conforme necessário
            reportData.add(dto);
        }

        reportsTableView.setItems(reportData);
    }
}