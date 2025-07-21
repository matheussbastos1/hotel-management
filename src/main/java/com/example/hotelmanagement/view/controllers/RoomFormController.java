package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.controller.RoomController;
import com.example.hotelmanagement.models.Room;
import com.example.hotelmanagement.models.RoomStatus;
import com.example.hotelmanagement.models.RoomType;
import com.example.hotelmanagement.repository.repositoryExceptions.RoomNotFoundException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class RoomFormController {

    @FXML private TextField roomNumberField;
    @FXML private ComboBox<String> roomTypeComboBox;
    @FXML private TextField priceField;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private TextField maxOccupancyField;
    @FXML private TextField bedTypeField;

    private RoomController roomController;
    private Room roomToEdit;

    public void setRoomController(RoomController roomController) {
        this.roomController = roomController;
    }

    public void setRoom(Room room) {
        this.roomToEdit = room;
        if (roomToEdit != null) {
            roomNumberField.setText(String.valueOf(roomToEdit.getRoomNumber()));
            roomNumberField.setEditable(false);
            roomTypeComboBox.getSelectionModel().select(roomToEdit.getRoomType().name());
            priceField.setText(String.valueOf(roomToEdit.getPrice()));
            statusComboBox.getSelectionModel().select(roomToEdit.getStatus().name());
            maxOccupancyField.setText(String.valueOf(roomToEdit.getMaxOccupancy()));
            bedTypeField.setText(roomToEdit.getBedType());
        }
    }

    @FXML
    public void initialize() {
        roomTypeComboBox.setItems(FXCollections.observableArrayList(
                Arrays.stream(RoomType.values())
                        .map(Enum::name)
                        .collect(Collectors.toList())
        ));

        statusComboBox.setItems(FXCollections.observableArrayList(
                Arrays.stream(RoomStatus.values())
                        .map(Enum::name)
                        .collect(Collectors.toList())
        ));
    }

    @FXML
    private void handleSave(ActionEvent event) {
        System.out.println("Iniciando salvamento do quarto...");

        if (validateFields()) {
            try {
                int roomNumber = Integer.parseInt(roomNumberField.getText().trim());
                RoomType roomType = RoomType.valueOf(roomTypeComboBox.getValue());
                double price = Double.parseDouble(priceField.getText().trim());
                RoomStatus status = RoomStatus.valueOf(statusComboBox.getValue());
                int maxOccupancy = Integer.parseInt(maxOccupancyField.getText().trim());
                String bedType = bedTypeField.getText().trim();

                System.out.println("Criando quarto: " + roomNumber);
                Room room = new Room(roomNumber, roomType, price, status, maxOccupancy, bedType);

                if (roomToEdit == null) {
                    System.out.println("Adicionando novo quarto...");
                    roomController.add(room);
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Quarto adicionado com sucesso!");
                } else {
                    System.out.println("Atualizando quarto existente...");
                    roomController.update(room);
                    showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Quarto atualizado com sucesso!");
                }

                System.out.println("Quarto salvo com sucesso!");

            } catch (NumberFormatException e) {
                System.err.println("Erro de formato numérico: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro de Entrada", "Por favor, insira valores numéricos válidos.");
            } catch (IllegalArgumentException e) {
                System.err.println("Erro de argumento: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro de Seleção", "Por favor, selecione valores válidos.");
            } catch (RoomNotFoundException e) {
                System.err.println("Quarto não encontrado: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro", e.getMessage());
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
            }
        } else {
            System.out.println("Validação dos campos falhou");
        }
    }

    @FXML
    private void handleVoltar(ActionEvent event) {
        try {
            // Simplesmente fecha a janela atual do formulário
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

            // O Dashboard já estava aberto, então não precisamos criar uma nova instância

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível fechar a janela: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeForm();
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (roomNumberField.getText().trim().isEmpty()) {
            errorMessage.append("Número do Quarto não pode estar em branco.\n");
        } else {
            try {
                Integer.parseInt(roomNumberField.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage.append("Número do Quarto deve ser um número inteiro.\n");
            }
        }

        if (roomTypeComboBox.getValue() == null || roomTypeComboBox.getValue().isEmpty()) {
            errorMessage.append("Tipo do Quarto deve ser selecionado.\n");
        }

        if (priceField.getText().trim().isEmpty()) {
            errorMessage.append("Preço não pode estar em branco.\n");
        } else {
            try {
                Double.parseDouble(priceField.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage.append("Preço deve ser um valor numérico.\n");
            }
        }

        if (statusComboBox.getValue() == null || statusComboBox.getValue().isEmpty()) {
            errorMessage.append("Status deve ser selecionado.\n");
        }

        if (maxOccupancyField.getText().trim().isEmpty()) {
            errorMessage.append("Ocupação Máxima não pode estar em branco.\n");
        } else {
            try {
                Integer.parseInt(maxOccupancyField.getText().trim());
            } catch (NumberFormatException e) {
                errorMessage.append("Ocupação Máxima deve ser um número inteiro.\n");
            }
        }

        if (bedTypeField.getText().trim().isEmpty()) {
            errorMessage.append("Tipo de Cama não pode estar em branco.\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Campos Inválidos", errorMessage.toString());
            return false;
        }

        return true;
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