package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.Room;
import com.example.hotelmanagement.models.ReservationStatus;
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
        System.out.println("\n--- DENTRO DO setData DO CARD ---");
        System.out.println("O objeto reservation recebido é nulo? " + (reservation == null));
        System.out.println("O label guestNameLabel é nulo? " + (guestNameLabel == null));
        System.out.println("O label reservationDetailsLabel é nulo? " + (reservationDetailsLabel == null));
        System.out.println("O label statusLabel é nulo? " + (statusLabel == null));
        System.out.println("---------------------------------");
        this.reservation = reservation;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // --- VERIFICAÇÃO DO HÓSPEDE ---
        Guest guest = reservation.getPrincipalGuest();
        if (guest != null && guest.getName() != null) {
            guestNameLabel.setText(guest.getName());
        } else {
            guestNameLabel.setText("[Hóspede não definido]");
        }

        // --- VERIFICAÇÃO DAS DATAS E QUARTO ---
        LocalDate checkIn = reservation.getCheckInDate();
        LocalDate checkOut = reservation.getCheckOutDate();
        Room room = reservation.getRoom();

        String checkInText = (checkIn != null) ? checkIn.format(formatter) : "[Data Inválida]";
        String checkOutText = (checkOut != null) ? checkOut.format(formatter) : "[Data Inválida]";
        String roomText = (room != null) ? room.toString() : "[Quarto não definido]";

        reservationDetailsLabel.setText(
                "Check-in: " + checkInText + " - Check-out: " + checkOutText + " | Quarto: " + roomText
        );

        // --- VERIFICAÇÃO DO STATUS ---
        ReservationStatus status = reservation.getStatus();
        if (status != null) {
            statusLabel.setText(status.toString());
            // Lógica para a cor do status
            if (status == ReservationStatus.BEGGAR) { // Usando o status que você definiu
                statusLabel.getStyleClass().setAll("card-status-confirmed"); // Mantenha a classe ou crie uma nova
            } else {
                statusLabel.getStyleClass().setAll("card-status-pending");
            }
        } else {
            statusLabel.setText("[Status não definido]");
        }
    }

    @FXML
    private void handleEfetivarCheckin() {
        // A lógica de efetivar o check-in virá aqui
        System.out.println("Clicou em Efetivar Check-in");
    }

    @FXML
    private void handleCancelarReserva() {
        // A lógica de cancelar a reserva virá aqui
        System.out.println("Clicou em Cancelar Reserva");
    }
}