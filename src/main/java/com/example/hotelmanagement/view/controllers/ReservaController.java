package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.Room;
import com.example.hotelmanagement.repository.GuestRepository;
import com.example.hotelmanagement.repository.ReservationRepository;
import com.example.hotelmanagement.repository.RoomRepository;
import com.example.hotelmanagement.repository.impl.GuestRepositoryImpl;
import com.example.hotelmanagement.repository.impl.ReservationRepositoryImpl;
import com.example.hotelmanagement.repository.impl.RoomRepositoryImpl;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReservaController {

    // --- Repositórios para Acesso aos Dados ---
    private final GuestRepository guestRepository = new GuestRepositoryImpl();
    private final ReservationRepository reservationRepository = new ReservationRepositoryImpl();
    private final RoomRepository roomRepository = new RoomRepositoryImpl();

    // --- Componentes da Tela (Ligados ao FXML) ---
    @FXML private TextField nameField;
    @FXML private TextField cpfld; // Adicionado para corresponder ao seu FXML
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private DatePicker checkInDatePicker;
    @FXML private DatePicker checkOutDatePicker;
    @FXML private ComboBox<Room> roomComboBox;
    @FXML private Spinner<Integer> hospedesSpinner;
    @FXML private VBox companionsVBox;

    // Lista para guardar a referência dos formulários de acompanhantes
    private final List<Node> companionForms = new ArrayList<>();

    @FXML
    public void initialize() {
        // Configura o Spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        hospedesSpinner.setValueFactory(valueFactory);

        // Adiciona o "ouvinte" que reage a mudanças no Spinner
        hospedesSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateCompanionForms(newValue));

        // Carrega a lista de quartos disponíveis
        loadAvailableRooms();
    }

    /**
     * Adiciona ou remove os "mini-formulários" de acompanhantes na tela.
     */
    private void updateCompanionForms(int totalHospedes) {
        companionsVBox.getChildren().clear();
        companionForms.clear();

        int numeroDeAcompanhantes = totalHospedes - 1;
        if (numeroDeAcompanhantes <= 0) return;

        for (int i = 0; i < numeroDeAcompanhantes; i++) {
            try {
                Parent companionFormNode = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/CompanionForm.fxml")));
                companionsVBox.getChildren().add(companionFormNode);
                companionForms.add(companionFormNode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Ação do botão "Salvar Reserva".
     */
    @FXML
    private void handleSalvarReserva() {
        try {
            Guest principalGuest = new Guest();
            principalGuest.setName(nameField.getText());
            principalGuest.setCpf(cpfld.getText());
            principalGuest.setEmail(emailField.getText());
            principalGuest.setPhone(phoneField.getText());
            principalGuest.setAddress(addressField.getText());

            guestRepository.addGuest(principalGuest);

            List<Guest> companionsList = new ArrayList<>();
            for (Node formNode : companionForms) {
                TextField name = (TextField) formNode.lookup("#companionNameField");
                TextField doc = (TextField) formNode.lookup("#companionDocField");

                if (name != null && !name.getText().isEmpty()) {
                    Guest companion = new Guest();
                    companion.setName(name.getText());
                    companion.setCpf(doc.getText());
                    companionsList.add(companion);
                    // Lembre-se: não salvamos acompanhantes no repositório de guests
                }
            }

            Reservation reservation = new Reservation();
            reservation.setPrincipalGuest(principalGuest);
            reservation.setCompanions(companionsList);
            reservation.setRoom(roomComboBox.getValue());
            reservation.setCheckInDate(checkInDatePicker.getValue());
            reservation.setCheckOutDate(checkOutDatePicker.getValue());

            reservationRepository.addReservation(reservation);

            showAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Reserva criada com sucesso.");
            limparCampos();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao salvar: " + e.getMessage());
        }
    }
    @FXML
    private void handleVoltar(javafx.event.ActionEvent event) {
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

    private void loadAvailableRooms() {
        List<Room> availableRooms = roomRepository.getAvailableRooms();
        roomComboBox.setItems(FXCollections.observableArrayList(availableRooms));
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void limparCampos() {
        nameField.clear();
        cpfld.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        checkInDatePicker.setValue(null);
        checkOutDatePicker.setValue(null);
        roomComboBox.getSelectionModel().clearSelection();
        hospedesSpinner.getValueFactory().setValue(1);
        companionsVBox.getChildren().clear();
        companionForms.clear();
    }
}