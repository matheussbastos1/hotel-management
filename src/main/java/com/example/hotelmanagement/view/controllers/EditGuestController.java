package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.models.Guest;
import com.example.hotelmanagement.repository.GuestRepository;
import com.example.hotelmanagement.repository.impl.GuestRepositoryImpl;
import com.example.hotelmanagement.repository.repositoryExceptions.GuestNotFoundException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditGuestController {

    private final GuestRepository guestRepository = new GuestRepositoryImpl();
    private Guest currentGuest;

    @FXML private TextField nameField;
    @FXML private TextField cpfField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;

    /**
     * Define o hóspede a ser editado e preenche os campos
     */
    public void setGuest(Guest guest) {
        this.currentGuest = guest;
        preencherCampos();
    }

    private void preencherCampos() {
        if (currentGuest != null) {
            nameField.setText(currentGuest.getName() != null ? currentGuest.getName() : "");
            cpfField.setText(currentGuest.getCpf() != null ? currentGuest.getCpf() : "");
            emailField.setText(currentGuest.getEmail() != null ? currentGuest.getEmail() : "");
            phoneField.setText(currentGuest.getPhone() != null ? currentGuest.getPhone() : "");
            addressField.setText(currentGuest.getAddress() != null ? currentGuest.getAddress() : "");
        }
    }

    @FXML
    private void handleSalvarEdicao() {
        try {
            // Validação básica
            if (nameField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Atenção", "O nome é obrigatório.");
                return;
            }

            if (cpfField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Atenção", "O CPF é obrigatório.");
                return;
            }

            // Atualiza os dados do hóspede
            currentGuest.setName(nameField.getText().trim());
            currentGuest.setCpf(cpfField.getText().trim());
            currentGuest.setEmail(emailField.getText().trim());
            currentGuest.setPhone(phoneField.getText().trim());
            currentGuest.setAddress(addressField.getText().trim());

            // Salva as alterações
            guestRepository.updateGuest(currentGuest);

            showAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Informações do hóspede atualizadas com sucesso.");

            // Volta para a tela anterior
            handleVoltar();

        } catch (GuestNotFoundException e) {
            showAlert(Alert.AlertType.ERROR, "Erro", "Hóspede não encontrado: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao salvar: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() {
        preencherCampos(); // Restaura os valores originais
    }

    @FXML
    private void handleVoltar() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GuestListView.fxml"));
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Lista de Hóspedes");
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