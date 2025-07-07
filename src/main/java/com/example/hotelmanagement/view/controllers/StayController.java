package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.controller.ReservationController;
import com.example.hotelmanagement.controller.RoomController;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.ReservationStatus;
import com.example.hotelmanagement.models.Room;
import com.example.hotelmanagement.models.RoomStatus;
import com.example.hotelmanagement.repository.impl.GuestRepositoryImpl;
import com.example.hotelmanagement.repository.impl.InvoiceRepositoryImpl;
import com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl;
import com.example.hotelmanagement.repository.impl.RoomRepositoryImpl;
import com.example.hotelmanagement.repository.repositoryExceptions.ReservationNotFoundException;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;
import com.example.hotelmanagement.service.impl.InvoiceServiceImpl;
import com.example.hotelmanagement.service.impl.ReservationServiceImpl;
import com.example.hotelmanagement.service.serviceExceptions.InvalidOperationException;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class StayController {

    @FXML private TextField searchField;
    @FXML private Label reservationIdLabel;
    @FXML private Label guestNameLabel;
    @FXML private Label roomDetailsLabel;
    @FXML private Label checkInDateLabel;
    @FXML private Label checkOutDateLabel;
    @FXML private Label reservationStatusLabel;
    @FXML private Button checkInButton;
    @FXML private Button checkOutButton;
    @FXML private Label messageLabel;

    private ReservationController reservationController;
    private RoomController roomController;
    private ReservationServiceImpl reservationService; // Usaremos o serviço para lógica de check-in/out

    private Reservation currentReservation; // Armazena a reserva carregada

    @FXML
    public void initialize() {
        // Inicialização dos controladores de negócio (injeção manual simplificada)
        // Em um projeto maior, usaria um framework de injeção de dependências (ex: Spring)
        // Ou um método de factory para os controladores.
        RoomRepositoryImpl roomRepository = new RoomRepositoryImpl();
        ReservationRepositoryImpl reservationRepository = new ReservationRepositoryImpl();
        InvoiceRepositoryImpl invoiceRepository = new InvoiceRepositoryImpl(); // Necessário para ReservationService
        GuestRepositoryImpl guestRepository = new GuestRepositoryImpl(); // Pode ser necessário para GuestService, se for usar

        // Popula alguns dados de exemplo para teste
        populateSampleData(roomRepository, reservationRepository, guestRepository);

        roomController = new RoomController(roomRepository);
        // O ReservationService precisa de repositórios e do InvoiceService
        InvoiceServiceImpl invoiceService = new InvoiceServiceImpl(invoiceRepository);
        reservationService = new ReservationServiceImpl(reservationRepository, roomRepository, invoiceRepository, invoiceService);
        reservationController = new ReservationController(reservationRepository);


        clearFields();
    }

    private void populateSampleData(RoomRepositoryImpl roomRepository, ReservationRepositoryImpl reservationRepository, GuestRepositoryImpl guestRepository) {
        // Adicionar alguns quartos
        Room room101 = new Room(101, com.example.hotelmanagement.models.RoomType.COUPLE, 250.0, RoomStatus.AVAILABLE, 2, "Queen");
        Room room102 = new Room(102, com.example.hotelmanagement.models.RoomType.INDIVIDUAL, 150.0, RoomStatus.AVAILABLE, 1, "Single");
        Room room201 = new Room(201, com.example.hotelmanagement.models.RoomType.FAMILY, 400.0, RoomStatus.AVAILABLE, 4, "King + 2 Singles");

        roomRepository.addRoom(room101);
        roomRepository.addRoom(room102);
        roomRepository.addRoom(room201);


        // Adicionar alguns hóspedes
        Guest guest1 = new Guest(1L, "João Silva", "joao@example.com", "11987654321", "Rua A, 123", "12345678901");
        Guest guest2 = new Guest(2L, "Maria Souza", "maria@example.com", "21998765432", "Av B, 456", "09876543210");
        guestRepository.addGuest(guest1);
        guestRepository.addGuest(guest2);


        // Adicionar algumas reservas
        // Reserva 1: Para check-in futuro (BOOKED)
        Reservation res1 = new Reservation(
                1001L, guest1, room101,
                LocalDate.now().plusDays(2), LocalDate.now().plusDays(5),
                ReservationStatus.BOOKED, null, null, guest1, null);
        reservationRepository.addReservation(res1);

        // Reserva 2: Para check-in hoje (BOOKED) - ideal para testar check-in
        Reservation res2 = new Reservation(
                1002L, guest2, room102,
                LocalDate.now(), LocalDate.now().plusDays(3),
                ReservationStatus.BOOKED, null, null, guest2, null);
        reservationRepository.addReservation(res2);

        // Reserva 3: Já com check-in realizado (CHECKED_IN) - ideal para testar check-out
        Reservation res3 = new Reservation(
                1003L, guest1, room201,
                LocalDate.now().minusDays(2), LocalDate.now().plusDays(1),
                ReservationStatus.CHECKED_IN, null, null, guest1, null);
        try {
            // Simula o quarto ocupado pelo check-in
            Room room201Occupied = roomRepository.findRoomByNumber(201);
            if(room201Occupied != null) {
                room201Occupied.setStatus(RoomStatus.OCCUPIED);
                roomRepository.updateRoom(room201Occupied);
            }
        } catch (RoomNotFoundException e) {
            System.err.println("Erro ao atualizar quarto para dados de amostra: " + e.getMessage());
        }
        reservationRepository.addReservation(res3);
    }


    @FXML
    private void handleSearch() {
        clearFields();
        messageLabel.setText("");
        String searchText = searchField.getText().trim();
        if (searchText.isEmpty()) {
            messageLabel.setText("Por favor, digite o número da reserva ou CPF do hóspede.");
            return;
        }

        try {
            // Tenta buscar por ID da Reserva
            long reservationId = Long.parseLong(searchText);
            Optional<Reservation> foundReservation = reservationController.findById((int) reservationId);
            if (foundReservation.isPresent()) {
                currentReservation = foundReservation.get();
                displayReservationDetails(currentReservation);
            } else {
                messageLabel.setText("Reserva com ID " + reservationId + " não encontrada.");
                currentReservation = null;
            }
        } catch (NumberFormatException e) {
            // Se não for um número, tenta buscar por CPF (não implementado no ReservationController,
            // então farei uma busca simples em memória para este exemplo)
            try {
                Optional<Reservation> foundReservationByCpf = reservationController.findAll().stream()
                        .filter(r -> r.getGuest() != null && r.getGuest().getCpf().equals(searchText))
                        .findFirst();

                if (foundReservationByCpf.isPresent()) {
                    currentReservation = foundReservationByCpf.get();
                    displayReservationDetails(currentReservation);
                } else {
                    messageLabel.setText("Reserva ou hóspede com CPF " + searchText + " não encontrado.");
                    currentReservation = null;
                }
            } catch (Exception ex) {
                messageLabel.setText("Erro ao buscar reservas: " + ex.getMessage());
                currentReservation = null;
            }
        } catch (ReservationNotFoundException e) {
            messageLabel.setText("Reserva não encontrada: " + e.getMessage());
            currentReservation = null;
        } catch (Exception e) {
            messageLabel.setText("Erro inesperado ao buscar reserva: " + e.getMessage());
            e.printStackTrace();
            currentReservation = null;
        }
    }

    private void displayReservationDetails(Reservation reservation) {
        reservationIdLabel.setText(String.valueOf(reservation.getReservationId()));
        guestNameLabel.setText(reservation.getGuest() != null ? reservation.getGuest().getName() : "N/A");
        roomDetailsLabel.setText(reservation.getRoom() != null ?
                "Quarto " + reservation.getRoom().getRoomNumber() + " (" + reservation.getRoom().getRoomType().name() + ")" : "N/A");
        checkInDateLabel.setText(reservation.getCheckInDate() != null ? reservation.getCheckInDate().toString() : "N/A");
        checkOutDateLabel.setText(reservation.getCheckOutDate() != null ? reservation.getCheckOutDate().toString() : "N/A");
        reservationStatusLabel.setText(reservation.getStatus() != null ? reservation.getStatus().name() : "N/A");

        updateActionButtons(reservation.getStatus());
    }

    private void updateActionButtons(ReservationStatus status) {
        checkInButton.setDisable(true);
        checkOutButton.setDisable(true);

        if (currentReservation != null) {
            LocalDate today = LocalDate.now();
            LocalDate checkIn = currentReservation.getCheckInDate();
            LocalDate checkOut = currentReservation.getCheckOutDate();

            if (status == ReservationStatus.BOOKED && (checkIn.isEqual(today) || checkIn.isBefore(today))) {
                checkInButton.setDisable(false);
            } else if (status == ReservationStatus.CHECKED_IN && (checkOut.isEqual(today) || checkOut.isBefore(today))) {
                // Aqui podemos adicionar uma verificação de fatura se for essencial antes do check-out
                // Por simplicidade, vamos habilitar direto por enquanto, como discutido.
                checkOutButton.setDisable(false);
            }
        }
    }

    @FXML
    private void handleCheckIn() {
        if (currentReservation == null) {
            messageLabel.setText("Nenhuma reserva selecionada para check-in.");
            return;
        }
        try {
            // Usa o ReservationService para a lógica de negócio
            Reservation updatedReservation = reservationService.performCheckIn(currentReservation.getReservationId());
            displayReservationDetails(updatedReservation); // Atualiza a UI com o novo status
            messageLabel.setText("Check-in realizado com sucesso para a Reserva #" + updatedReservation.getReservationId());
        } catch (ReservationNotFoundException | InvalidOperationException e) {
            messageLabel.setText("Erro ao realizar check-in: " + e.getMessage());
        } catch (Exception e) {
            messageLabel.setText("Ocorreu um erro inesperado durante o check-in: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCheckOut() {
        if (currentReservation == null) {
            messageLabel.setText("Nenhuma reserva selecionada para check-out.");
            return;
        }

        try {
            // Usa o ReservationService para a lógica de negócio
            Reservation updatedReservation = reservationService.performCheckOut(currentReservation.getReservationId());
            displayReservationDetails(updatedReservation); // Atualiza a UI com o novo status
            messageLabel.setText("Check-out realizado com sucesso para a Reserva #" + updatedReservation.getReservationId());
        } catch (ReservationNotFoundException | InvalidOperationException e) {
            messageLabel.setText("Erro ao realizar check-out: " + e.getMessage());
        } catch (Exception e) {
            messageLabel.setText("Ocorreu um erro inesperado durante o check-out: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void handleVoltar(ActionEvent event) {
        try {
            // Este caminho volta para o Dashboard Principal
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/DashboardForm.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearFields() {
        reservationIdLabel.setText("-");
        guestNameLabel.setText("-");
        roomDetailsLabel.setText("-");
        checkInDateLabel.setText("-");
        checkOutDateLabel.setText("-");
        reservationStatusLabel.setText("-");
        checkInButton.setDisable(true);
        checkOutButton.setDisable(true);
        messageLabel.setText("");
    }
}