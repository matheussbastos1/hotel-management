package com.example.hotelmanagement.view;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.repository.GuestRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class GuestRegistrationController {

    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private GuestRepository guestRepository;

    public void setGuestRepository(GuestRepository guestRepository) {
        this.guestRepository = guestRepository;
    }

    @FXML
    private void handleSave(ActionEvent event) {
        if (validateFields()) {
            try {
                Guest guest = new Guest();
                guest.setId((long) Integer.parseInt(idField.getText().trim()));
                guest.setName(nameField.getText().trim());
                guest.setEmail(emailField.getText().trim());
                guest.setPhone(phoneField.getText().trim());
                guest.setAddress(addressField.getText().trim());

                guestRepository.addGuest(guest);

                showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Hóspede cadastrado com sucesso!");
                closeForm();
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "ID deve ser um número válido!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao cadastrar hóspede: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeForm();
    }

    private boolean validateFields() {
        StringBuilder errorMessage = new StringBuilder();

        if (idField.getText().trim().isEmpty()) {
            errorMessage.append("ID não pode estar em branco.\n");
        }

        if (nameField.getText().trim().isEmpty()) {
            errorMessage.append("Nome não pode estar em branco.\n");
        }

        if (emailField.getText().trim().isEmpty()) {
            errorMessage.append("Email não pode estar em branco.\n");
        }

        if (phoneField.getText().trim().isEmpty()) {
            errorMessage.append("Telefone não pode estar em branco.\n");
        }

        if (addressField.getText().trim().isEmpty()) {
            errorMessage.append("Endereço não pode estar em branco.\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Campos inválidos", errorMessage.toString());
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
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}