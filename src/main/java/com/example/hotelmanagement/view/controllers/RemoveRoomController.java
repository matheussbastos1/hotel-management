package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.controller.RoomController;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class RemoveRoomController {

    @FXML private TextField roomNumberField;
    @FXML private Label statusLabel;

    private RoomController roomController;

    public void setRoomController(RoomController roomController) {
        this.roomController = roomController;
    }

    @FXML
    public void initialize() {
        // Configurações iniciais se necessário
    }

    @FXML
    private void handleRemoveRoom(ActionEvent event) {
        String roomNumberText = roomNumberField.getText().trim();

        if (roomNumberText.isEmpty()) {
            showStatusMessage("Por favor, digite o número do quarto.", true);
            return;
        }

        int roomNumber;
        try {
            roomNumber = Integer.parseInt(roomNumberText);
        } catch (NumberFormatException e) {
            showStatusMessage("Número do quarto inválido. Digite apenas números.", true);
            return;
        }

        // Confirmação antes de remover
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmar Remoção");
        confirmAlert.setHeaderText("Remover Quarto");
        confirmAlert.setContentText("Tem certeza de que deseja remover o quarto " +
                roomNumber + "?\n\nEsta ação não pode ser desfeita.");

        Optional<ButtonType> result = confirmAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                System.out.println("Removendo quarto: " + roomNumber);
                roomController.remove(roomNumber);

                showAlert(Alert.AlertType.INFORMATION, "Sucesso",
                        "Quarto " + roomNumber + " removido com sucesso!");

                // Limpar campo e fechar janela
                roomNumberField.clear();
                closeForm();

            } catch (RoomNotFoundException e) {
                System.err.println("Quarto não encontrado para remoção: " + e.getMessage());
                showStatusMessage("Quarto " + roomNumber + " não foi encontrado.", true);
            } catch (Exception e) {
                System.err.println("Erro inesperado ao remover quarto: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro",
                        "Ocorreu um erro ao remover o quarto: " + e.getMessage());
            }
        } else {
            System.out.println("Remoção cancelada pelo usuário");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeForm();
    }

    private void showStatusMessage(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setVisible(true);
        if (isError) {
            statusLabel.setStyle("-fx-text-fill: red;");
        } else {
            statusLabel.setStyle("-fx-text-fill: green;");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeForm() {
        Stage stage = (Stage) roomNumberField.getScene().getWindow();
        stage.close();
    }
}