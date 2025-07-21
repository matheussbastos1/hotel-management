package com.example.hotelmanagement.view.controllers;



import com.example.hotelmanagement.models.Reservation;

import com.example.hotelmanagement.models.Room;

import com.example.hotelmanagement.models.RoomStatus;

import com.example.hotelmanagement.repository.ReservationRepository;

import com.example.hotelmanagement.repository.RoomRepository;

import com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl;

import com.example.hotelmanagement.repository.impl.RoomRepositoryImpl;

import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;

import com.example.hotelmanagement.view.controllers.ServicesController.ServicoManager;

import com.example.hotelmanagement.view.controllers.ServicesController.LancamentoServico;

import javafx.collections.FXCollections;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;

import javafx.scene.Node;

import javafx.scene.Parent;

import javafx.scene.Scene;

import javafx.scene.control.*;

import javafx.scene.layout.VBox;

import javafx.stage.Stage;



import java.io.IOException;

import java.time.temporal.ChronoUnit;

import java.util.List;

import java.util.Optional;



public class CheckOutController {



    private final RoomRepository roomRepository = RoomRepositoryImpl.getInstance();

    private final ReservationRepository reservationRepository = ReservationRepositoryImpl.getInstance();



    @FXML private ComboBox<Room> occupiedRoomsComboBox;

    @FXML private VBox checkoutDetailsPane;

    @FXML private Label guestInfoLabel;

    @FXML private Label stayDetailsLabel;

    @FXML private Label staySubtotalLabel;

    @FXML private ListView<String> servicesListView;

    @FXML private Label servicesSubtotalLabel;

    @FXML private Label totalAmountLabel;

    @FXML private Button finalizeButton;

    @FXML private Button obterPagamentoButton;



    private Reservation currentReservation;

    private double totalCost = 0.0;



    @FXML

    public void initialize() {

        List<Room> occupiedRooms = roomRepository.getRoomsByStatus(RoomStatus.OCCUPIED);

        occupiedRoomsComboBox.setItems(FXCollections.observableArrayList(occupiedRooms));



        occupiedRoomsComboBox.getSelectionModel().selectedItemProperty().addListener(

                (obs, oldVal, newVal) -> {

                    if (newVal != null) {

                        loadCheckoutDetailsForRoom(newVal);

                    }

                }

        );

    }



    private void loadCheckoutDetailsForRoom(Room room) {

        Optional<Reservation> reservationOpt = reservationRepository.findActiveByRoomNumber(room.getRoomNumber());



        if (reservationOpt.isPresent()) {

            currentReservation = reservationOpt.get();



            long numberOfNights = ChronoUnit.DAYS.between(currentReservation.getCheckInDate(), currentReservation.getCheckOutDate());

            if (numberOfNights == 0) numberOfNights = 1;

            double stayCost = numberOfNights * room.getPrice();



// BUSCA OS SERVIÇOS LANÇADOS PARA ESTE QUARTO

            String roomKey = room.toString();

            List<LancamentoServico> servicosLancados = ServicoManager.getServicosPorQuarto(roomKey);

            double servicesCost = ServicoManager.getTotalServicosPorQuarto(roomKey);



            totalCost = stayCost + servicesCost;



            guestInfoLabel.setText("Hóspede: " + currentReservation.getPrincipalGuest().getName());

            stayDetailsLabel.setText(numberOfNights + " noite(s) x R$ " + String.format("%.2f", room.getPrice()));

            staySubtotalLabel.setText("Subtotal Estadia: R$ " + String.format("%.2f", stayCost));



// POPULA A LISTA DE SERVIÇOS

            servicesListView.getItems().clear();

            servicosLancados.forEach(servico ->

                    servicesListView.getItems().add(servico.toString())

            );



            servicesSubtotalLabel.setText("Subtotal Serviços: R$ " + String.format("%.2f", servicesCost));

            totalAmountLabel.setText("TOTAL A PAGAR: R$ " + String.format("%.2f", totalCost));



            checkoutDetailsPane.setVisible(true);

            checkoutDetailsPane.setManaged(true);

            finalizeButton.setDisable(false);

            obterPagamentoButton.setDisable(false);

        } else {

            checkoutDetailsPane.setVisible(false);

            checkoutDetailsPane.setManaged(false);

            finalizeButton.setDisable(true);

            obterPagamentoButton.setDisable(true);

            showAlert(Alert.AlertType.ERROR, "Erro", "Nenhuma reserva ativa encontrada para o quarto " + room.getRoomNumber());

        }

    }



    @FXML

    private void handleFinalizarCheckOut() {

        if (currentReservation == null) return;



        double totalAPagar = totalCost;



        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Confirmação de Pagamento");

        alert.setHeaderText("Finalizar o Check-out do quarto " + currentReservation.getRoom().getRoomNumber() + "?");

        alert.setContentText("O valor total da conta é R$ " + String.format("%.2f", totalAPagar) + ".\n\nVocê confirma o recebimento do pagamento?");



        ButtonType buttonTypeSim = new ButtonType("Sim, Finalizar");

        ButtonType buttonTypeNao = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);



        alert.showAndWait().ifPresent(response -> {

            if (response == buttonTypeSim) {

                Room roomToCheckout = currentReservation.getRoom();

                roomToCheckout.setStatus(RoomStatus.AVAILABLE);

                try {

                    roomRepository.updateRoom(roomToCheckout);



// LIMPA OS SERVIÇOS APÓS O CHECKOUT

                    ServicoManager.limparServicosPorQuarto(roomToCheckout.toString());



                } catch (RoomNotFoundException e) {

                    showAlert(Alert.AlertType.ERROR, "Erro", "Quarto não encontrado para atualização.");

                }



                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Check-out realizado com sucesso!");

                resetScreen();

            }

        });

    }



    @FXML

    private void handleObterPagamento(ActionEvent event) {

        if (currentReservation == null) {

            showAlert(Alert.AlertType.ERROR, "Erro", "Selecione um quarto ocupado para obter o pagamento.");

            return;

        }

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/payment-screen.fxml"));

            Parent paymentRoot = loader.load();

            PaymentController paymentController = loader.getController();

            paymentController.setAmount(totalCost);



            Stage stage = new Stage();

            stage.setTitle("Pagamento");

            stage.setScene(new Scene(paymentRoot));

            stage.show();



            Stage checkoutStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            checkoutStage.close();

        } catch (IOException e) {

            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível abrir a tela de pagamento.");

        }

    }



    private void resetScreen() {

        occupiedRoomsComboBox.getSelectionModel().clearSelection();

        checkoutDetailsPane.setVisible(false);

        checkoutDetailsPane.setManaged(false);

        finalizeButton.setDisable(true);

        obterPagamentoButton.setDisable(true);

        currentReservation = null;

        List<Room> occupiedRooms = roomRepository.getRoomsByStatus(RoomStatus.OCCUPIED);

        occupiedRoomsComboBox.setItems(FXCollections.observableArrayList(occupiedRooms));

    }



    @FXML

    private void handleVoltar(ActionEvent event) {

        try {

            Parent dashboardRoot = FXMLLoader.load(getClass().getResource("/DashboardForm.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(dashboardRoot);

            stage.setScene(scene);

            stage.setTitle("Tela Principal");

            stage.show();

        } catch (IOException e) {

            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível voltar para a tela principal.");

        }

    }



    private void showAlert(Alert.AlertType type, String title, String msg) {

        Alert alert = new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(msg);

        alert.showAndWait();

    }

}
