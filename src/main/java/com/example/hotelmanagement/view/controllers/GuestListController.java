package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.repository.GuestRepository;
import com.example.hotelmanagement.repository.impl.GuestRepositoryImpl;
import com.example.hotelmanagement.repository.repositoryExceptions.GuestNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GuestListController {

    private final GuestRepository guestRepository = new GuestRepositoryImpl();
    private ObservableList<Guest> allGuests = FXCollections.observableArrayList();
    private ObservableList<Guest> filteredGuests = FXCollections.observableArrayList();

    @FXML private TextField searchField;
    @FXML private TableView<Guest> guestsTableView;
    @FXML private TableColumn<Guest, Long> idColumn;
    @FXML private TableColumn<Guest, String> nameColumn;
    @FXML private TableColumn<Guest, String> cpfColumn;
    @FXML private TableColumn<Guest, String> emailColumn;
    @FXML private TableColumn<Guest, String> phoneColumn;
    @FXML private TableColumn<Guest, Void> actionsColumn;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupActionsColumn();
        loadGuests();
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        actionsColumn.setPrefWidth(210.0);
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<Guest, Void>() {
            private final Button editButton = new Button("Editar");
            private final Button deleteButton = new Button("Excluir");
            private final HBox container = new HBox(5);

            {
                editButton.getStyleClass().add("secondary-button");
                deleteButton.getStyleClass().add("danger-button");
                container.getChildren().addAll(editButton, deleteButton);

                editButton.setOnAction(event -> {
                    Guest guest = getTableView().getItems().get(getIndex());
                    handleEditarHospede(guest);
                });

                deleteButton.setOnAction(event -> {
                    Guest guest = getTableView().getItems().get(getIndex());
                    handleExcluirHospede(guest);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(container);
                }
            }
        });
    }

    private void loadGuests() {
        try {
            List<Guest> guests = guestRepository.getGuests();
            allGuests.setAll(guests);
            filteredGuests.setAll(guests);
            guestsTableView.setItems(filteredGuests);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar hóspedes: " + e.getMessage());
        }
    }

    @FXML
    private void handleBuscar() {
        String searchText = searchField.getText().toLowerCase().trim();

        if (searchText.isEmpty()) {
            filteredGuests.setAll(allGuests);
        } else {
            List<Guest> filtered = allGuests.stream()
                    .filter(guest ->
                            (guest.getName() != null && guest.getName().toLowerCase().contains(searchText)) ||
                                    (guest.getCpf() != null && guest.getCpf().contains(searchText))
                    )
                    .collect(Collectors.toList());
            filteredGuests.setAll(filtered);
        }
    }

    @FXML
    private void handleLimparBusca() {
        searchField.clear();
        filteredGuests.setAll(allGuests);
    }

    private void handleEditarHospede(Guest guest) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditGuestView.fxml"));
            Parent root = loader.load();

            EditGuestController controller = loader.getController();
            controller.setGuest(guest);

            Stage stage = (Stage) guestsTableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Hóspede");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela de edição: " + e.getMessage());
        }
    }

    private void handleExcluirHospede(Guest guest) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmar Exclusão");
        confirmAlert.setHeaderText("Excluir Hóspede");
        confirmAlert.setContentText("Tem certeza que deseja excluir o hóspede " + guest.getName() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    guestRepository.removeGuest(guest.getId().intValue());
                    loadGuests(); // Recarrega a lista
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Hóspede excluído com sucesso.");
                } catch (GuestNotFoundException e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Hóspede não encontrado: " + e.getMessage());
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao excluir hóspede: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void handleNovoHospede() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GeneralReserve.fxml"));
            Stage stage = (Stage) guestsTableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Nova Reserva");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela de cadastro: " + e.getMessage());
        }
    }

    @FXML
    private void handleVoltar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/DashboardForm.fxml"));
            Stage stage = (Stage) guestsTableView.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard Principal");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao voltar: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}