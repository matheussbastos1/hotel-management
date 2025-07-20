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
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckInController {

    @FXML private TextField searchField;
    @FXML private VBox cardsContainer;

    private final ReservationRepository reservationRepository = new ReservationRepositoryImpl();
    private List<Node> allReservationCards = new ArrayList<>();

    @FXML
    public void initialize() {
        loadPendingReservations();
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterCards(newVal));
    }

    private void loadPendingReservations() {
        cardsContainer.getChildren().clear();
        allReservationCards.clear();

        List<Reservation> pendingReservations = reservationRepository.findByStatus(ReservationStatus.BEGGAR);

        List<Room> pendingRooms = pendingReservations.stream()
                .map(Reservation::getRoom)
                .collect(Collectors.toList());

        for (Reservation reservation : pendingReservations) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/CardReserva.fxml"));
                Node cardNode = loader.load();

                CardReservaController cardController = loader.getController();
                cardController.setData(reservation);

                allReservationCards.add(cardNode);
                cardsContainer.getChildren().add(cardNode);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void filterCards(String searchText) {
        cardsContainer.getChildren().clear();

        if (searchText == null || searchText.isEmpty()) {
            cardsContainer.getChildren().addAll(allReservationCards);
            return;
        }

        // Lógica de filtro pode ser aprimorada conforme necessidade
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