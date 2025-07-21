package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.models.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CardReservaController {

    @FXML private Label guestNameLabel;
    @FXML private Label reservationDetailsLabel;
    @FXML private Label statusLabel;
    @FXML private Button cancelButton;
    @FXML private Button checkinButton;

    private Reservation reservation;

    /**
     * Este método agora verifica se os dados existem antes de usá-los.
     */
    public void setData(Reservation reservation) {
        this.reservation = reservation;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // --- NOME DO HÓSPEDE PRINCIPAL ---
        Guest guest = reservation.getPrincipalGuest();
        if (guest != null && guest.getName() != null && !guest.getName().trim().isEmpty()) {
            guestNameLabel.setText(guest.getName());
        } else {
            guestNameLabel.setText("Hóspede não informado");
        }

        // --- DETALHES DA RESERVA ---
        LocalDate checkIn = reservation.getCheckInDate();
        LocalDate checkOut = reservation.getCheckOutDate();
        Room room = reservation.getRoom();

        String checkInText = (checkIn != null) ? checkIn.format(formatter) : "Data inválida";
        String checkOutText = (checkOut != null) ? checkOut.format(formatter) : "Data inválida";
        String roomText = (room != null) ? "Quarto " + room.getRoomNumber() : "Quarto não definido";

        reservationDetailsLabel.setText(
                String.format("Check-in: %s - Check-out: %s | %s", checkInText, checkOutText, roomText)
        );

        // --- STATUS DA RESERVA ---
        ReservationStatus status = reservation.getStatus();
        if (status != null) {
            switch (status) {
                case BEGGAR:
                    statusLabel.setText("PENDENTE");
                    statusLabel.getStyleClass().setAll("card-status-pending");
                    break;
                case CHECKED_IN:
                    statusLabel.setText("CHECK-IN FEITO");
                    statusLabel.getStyleClass().setAll("card-status-confirmed");
                    break;
                case CANCELLED:
                    statusLabel.setText("CANCELADA");
                    statusLabel.getStyleClass().setAll("card-status-cancelled");
                    break;
                default:
                    statusLabel.setText(status.toString());
                    statusLabel.getStyleClass().setAll("card-status-confirmed");
            }
        } else {
            statusLabel.setText("Status indefinido");
            statusLabel.getStyleClass().setAll("card-status-pending");
        }

        // Desabilita botões se a reserva não estiver pendente
        if (status != ReservationStatus.BEGGAR) {
            checkinButton.setDisable(true);
            cancelButton.setDisable(true);
        }
    }

    @FXML
    private void handleEfetivarCheckin() {
        if (reservation == null) return;
        try {
            // Atualiza o status da reserva
            reservation.setStatus(ReservationStatus.CHECKED_IN);
            reservation.setUpdatedAt(java.time.LocalDateTime.now());
            com.example.hotelmanagement.repository.ReservationRepository repo =
                    com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl.getInstance();
            repo.updateReservation(reservation);

            // Atualiza o status do quarto para OCUPADO
            Room room = reservation.getRoom();
            room.setStatus(RoomStatus.OCCUPIED);
            com.example.hotelmanagement.repository.RoomRepository roomRepo =
                    com.example.hotelmanagement.repository.impl.RoomRepositoryImpl.getInstance();
            roomRepo.updateRoom(room);

            // Feedback visual
            statusLabel.setText("CHECKED_IN");
            statusLabel.getStyleClass().setAll("card-status-pending");
            checkinButton.setDisable(true);
            cancelButton.setDisable(true);
            System.out.println("Check-in realizado com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao efetivar check-in: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelarReserva() {
        if (reservation == null) return;
        try {
            // Atualiza o status da reserva para CANCELLED
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservation.setUpdatedAt(java.time.LocalDateTime.now());
            // Atualiza no repositório
            com.example.hotelmanagement.repository.ReservationRepository repo =
                    com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl.getInstance();
            repo.updateReservation(reservation);
            // Feedback visual
            statusLabel.setText("CANCELLED");
            statusLabel.getStyleClass().setAll("card-status-pending");
            checkinButton.setDisable(true);
            cancelButton.setDisable(true);
            System.out.println("Reserva cancelada com sucesso!");
        } catch (Exception e) {
            System.err.println("Erro ao cancelar reserva: " + e.getMessage());
        }
    }
}