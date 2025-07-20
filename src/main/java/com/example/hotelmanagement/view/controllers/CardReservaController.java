package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.ReservationStatus;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.time.format.DateTimeFormatter;

public class CardReservaController {

    @FXML private Label guestNameLabel;
    @FXML private Label reservationDetailsLabel;
    @FXML private Label statusLabel;
    @FXML private Button cancelButton;
    @FXML private Button checkinButton;

    private Reservation reservation;

    /**
     * Este método será chamado pelo controller principal para injetar os dados da reserva neste card.
     */
    public void setData(Reservation reservation) {
        this.reservation = reservation;

        // Formata as datas para um padrão brasileiro
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Preenche os labels com os dados da reserva
        guestNameLabel.setText(reservation.getPrincipalGuest().getName());
        reservationDetailsLabel.setText(
                "Check-in: " + reservation.getCheckInDate().format(formatter) +
                        " - Check-out: " + reservation.getCheckOutDate().format(formatter) +
                        " | Quarto: " + reservation.getRoom().toString() // Usando o toString() do Room que já fizemos
        );
        statusLabel.setText(reservation.getStatus().toString());

        // Mudar a cor do status (opcional, mas legal)
        if (reservation.getStatus().toString().equals("BEGGAR")) { // Use o nome exato do seu enum
            statusLabel.getStyleClass().add("card-status-confirmed");
        } else {
            statusLabel.getStyleClass().add("card-status-pending");
        }
    }

    @FXML
    private void handleEfetivarCheckin() {
    }

    @FXML
    private void handleCancelarReserva() {

        }

    }