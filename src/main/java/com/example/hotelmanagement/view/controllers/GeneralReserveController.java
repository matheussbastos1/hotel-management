package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.models.Reservation;
import com.example.hotelmanagement.models.ReservationStatus;
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
import java.time.LocalDate;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GeneralReserveController {

    // --- Repositórios para Acesso aos Dados ---
    private final GuestRepository guestRepository = new GuestRepositoryImpl();
    private final ReservationRepository reservationRepository = ReservationRepositoryImpl.getInstance();
    private final RoomRepository roomRepository = RoomRepositoryImpl.getInstance();

    // --- Componentes da Tela (Ligados ao FXML) ---
    @FXML private TextField nameField;
    @FXML private TextField cpfld;
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
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 1);
        hospedesSpinner.setValueFactory(valueFactory);
        hospedesSpinner.valueProperty().addListener((obs, oldValue, newValue) -> updateCompanionForms(newValue));
        findAndLoadAvailableRooms();
        roomComboBox.setDisable(true);
        roomComboBox.setPromptText("Primeiro, selecione as datas");

        // Adiciona "ouvintes" aos DatePickers. A busca de quartos só é feita quando as duas datas são válidas.
        checkInDatePicker.valueProperty().addListener((obs, oldV, newV) -> findAndLoadAvailableRooms());
        checkOutDatePicker.valueProperty().addListener((obs, oldV, newV) -> findAndLoadAvailableRooms());
    }

    private void findAndLoadAvailableRooms() {
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();

        // Só executa se ambas as datas foram selecionadas e a data de check-out é depois da de check-in
        if (checkInDate != null && checkOutDate != null && checkOutDate.isAfter(checkInDate)) {
            // Esta é a nova chamada que você precisará criar no seu repositório
            List<Room> availableRooms = roomRepository.findAvailableRoomsByDateRange(checkInDate, checkOutDate);

            roomComboBox.setItems(FXCollections.observableArrayList(availableRooms));
            roomComboBox.setDisable(false); // Habilita o ComboBox
            roomComboBox.setPromptText("Selecione um quarto");
        } else {
            roomComboBox.setDisable(true); // Mantém desabilitado se as datas forem inválidas
            roomComboBox.getItems().clear();
        }
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
   // src/main/java/com/example/hotelmanagement/view/controllers/GeneralReserveController.java
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
               }
           }

           Reservation reservation = new Reservation();
           reservation.setPrincipalGuest(principalGuest);
           reservation.setGuest(principalGuest); // <-- Corrigido: preenche o campo guest
           reservation.setCompanions(companionsList);
           reservation.setRoom(roomComboBox.getValue());
           reservation.setCheckInDate(checkInDatePicker.getValue());
           reservation.setCheckOutDate(checkOutDatePicker.getValue());
           reservation.setStatus(ReservationStatus.BEGGAR);
           reservation.setCreatedAt(java.time.LocalDateTime.now());
           reservation.setUpdatedAt(java.time.LocalDateTime.now());

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