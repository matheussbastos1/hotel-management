package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.ReservationStatus;
import com.example.hotelmanagement.models.Room;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckInController {

    @FXML private TextField searchField;
    @FXML private VBox cardsContainer;
    @FXML private VBox guestInfoPane;
    @FXML private Label selectedGuestNameLabel;
    @FXML private Label selectedGuestDetailsLabel;

    private final ReservationRepository reservationRepository = ReservationRepositoryImpl.getInstance();
    private List<Node> allReservationCards = new ArrayList<>();
    private Reservation selectedReservation;

    @FXML
    public void initialize() {
        loadPendingReservations();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterCards(newVal));
    }

    private void loadPendingReservations() {
        cardsContainer.getChildren().clear();
        allReservationCards.clear();

        List<Reservation> pendingReservations = reservationRepository.findByStatus(ReservationStatus.BEGGAR);

        for (Reservation reservation : pendingReservations) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardReserva.fxml"));
                Node cardNode = loader.load();

                CardReservaController cardController = loader.getController();
                cardController.setData(reservation);

                // Adiciona listener para seleção do card
                cardNode.setOnMouseClicked(event -> selectReservation(reservation));

                allReservationCards.add(cardNode);
                cardsContainer.getChildren().add(cardNode);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectReservation(Reservation reservation) {
        this.selectedReservation = reservation;
        updateGuestInfoDisplay();
    }

    private void updateGuestInfoDisplay() {
        if (selectedReservation != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // Nome do hóspede
            String guestName = selectedReservation.getPrincipalGuest() != null ?
                    selectedReservation.getPrincipalGuest().getName() : "Nome não informado";
            selectedGuestNameLabel.setText(guestName);

            // Detalhes da reserva
            String checkIn = selectedReservation.getCheckInDate() != null ?
                    selectedReservation.getCheckInDate().format(formatter) : "Data inválida";
            String checkOut = selectedReservation.getCheckOutDate() != null ?
                    selectedReservation.getCheckOutDate().format(formatter) : "Data inválida";
            String roomInfo = selectedReservation.getRoom() != null ?
                    "Quarto " + selectedReservation.getRoom().getRoomNumber() : "Quarto não definido";

            selectedGuestDetailsLabel.setText(
                    String.format("Check-in: %s | Check-out: %s | %s", checkIn, checkOut, roomInfo)
            );

            // Mostra o painel de informações
            guestInfoPane.setVisible(true);
            guestInfoPane.setManaged(true);
        } else {
            guestInfoPane.setVisible(false);
            guestInfoPane.setManaged(false);
        }
    }

    private void filterCards(String searchText) {
        cardsContainer.getChildren().clear();

        if (searchText == null || searchText.isEmpty()) {
            cardsContainer.getChildren().addAll(allReservationCards);
            return;
        }

        cardsContainer.getChildren().addAll(allReservationCards);
    }

    @FXML
    private void handleVoltar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/DashboardForm.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}