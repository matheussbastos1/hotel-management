package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.Room;
import com.example.hotelmanagement.models.RoomStatus;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.RoomRepository;
import com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl;
import com.example.hotelmanagement.repository.impl.RoomRepositoryImpl;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;
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
        // Carrega apenas os quartos que estão OCUPADOS
        List<Room> occupiedRooms = roomRepository.getRoomsByStatus(RoomStatus.OCCUPIED);
        occupiedRoomsComboBox.setItems(FXCollections.observableArrayList(occupiedRooms));

        // Adiciona um "ouvinte" para quando um quarto for selecionado
        occupiedRoomsComboBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        loadCheckoutDetailsForRoom(newVal);
                    }
                }
        );
    }

    /**
     * Busca os dados da reserva e calcula a conta quando um quarto é selecionado.
     */
    private void loadCheckoutDetailsForRoom(Room room) {
        // Encontra a reserva ativa para aquele quarto
        Optional<Reservation> reservationOpt = reservationRepository.findActiveByRoomNumber(room.getRoomNumber());

        if (reservationOpt.isPresent()) {
            currentReservation = reservationOpt.get();

            // --- Cálculos ---
            long numberOfNights = ChronoUnit.DAYS.between(currentReservation.getCheckInDate(), currentReservation.getCheckOutDate());
            if (numberOfNights == 0) numberOfNights = 1;
            double stayCost = numberOfNights * room.getPrice();
            double servicesCost = 0.0;
            totalCost = stayCost + servicesCost;
            guestInfoLabel.setText("Hóspede: " + currentReservation.getPrincipalGuest().getName());
            stayDetailsLabel.setText(numberOfNights + " noite(s) x R$ " + String.format("%.2f", room.getPrice()));
            staySubtotalLabel.setText("Subtotal Estadia: R$ " + String.format("%.2f", stayCost));

            // servicesListView.getItems().setAll(...); // Aqui você populária a lista de serviços
            servicesSubtotalLabel.setText("Subtotal Serviços: R$ " + String.format("%.2f", servicesCost));

            totalAmountLabel.setText("TOTAL A PAGAR: R$ " + String.format("%.2f", totalCost));

            // Mostra o painel de detalhes e habilita o botão
            checkoutDetailsPane.setVisible(true);
            checkoutDetailsPane.setManaged(true);
            finalizeButton.setDisable(false);
            obterPagamentoButton.setDisable(false);
        } else {
            // Caso não encontre uma reserva (situação de erro)
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

        double totalAPagar = Double.parseDouble(totalAmountLabel.getText().replaceAll("[^\\d.,]", "").replace(",", "."));

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação de Pagamento");
        alert.setHeaderText("Finalizar o Check-out do quarto " + currentReservation.getRoom().getRoomNumber() + "?");
        alert.setContentText("O valor total da conta é R$ " + String.format("%.2f", totalAPagar) + ".\n\nVocê confirma o recebimento do pagamento?");

        ButtonType buttonTypeSim = new ButtonType("Sim, Finalizar");
        ButtonType buttonTypeNao = new ButtonType("Não", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeSim, buttonTypeNao);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeSim) {
                // --- CORREÇÃO AQUI ---
                // Pega o quarto da reserva atual e manda o repositório atulizar su status.
                Room roomToCheckout = currentReservation.getRoom();
                roomToCheckout.setStatus(RoomStatus.AVAILABLE);
                try {
                    roomRepository.updateRoom(roomToCheckout);
                } catch (RoomNotFoundException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Quarto não encontrado para atualização.");
                }
                // Aqui você também atualizaria o status da reserva para FINALIZADA
                // reservationRepository.updateStatus(currentReservation.getId(), ReservationStatus.COMPLETED);

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

            // Fecha a tela de checkout
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
            // Carrega o arquivo FXML da tela de dashboard
            Parent dashboardRoot = FXMLLoader.load(getClass().getResource("/DashboardForm.fxml"));

            // Pega a janela atual (o Stage) a partir do evento do clique
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Cria uma nova cena com o conteúdo da tela de dashboard
            Scene scene = new Scene(dashboardRoot);

            // Define a nova cena na janela
            stage.setScene(scene);

            // Atualiza o título da janela
            stage.setTitle("Tela Principal");

            // Mostra a janela atualizada
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