package com.example.hotelmanagement.view.controllers;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;
import java.util.UUID;

public class PaymentController {

    @FXML private Label amountLabel;
    @FXML private ComboBox<String> paymentMethodComboBox;
    @FXML private Label statusLabel;

    @FXML private VBox cardPane;
    @FXML private VBox pixPane;
    @FXML private VBox bankSlipPane;
    @FXML private VBox bankTransferPane;

    @FXML private TextField cardHolderNameField;
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryDateField;
    @FXML private TextField cvvField;

    @FXML private TextField pixCodeField;
    @FXML private ImageView pixQrCodeImage;

    @FXML private Text bankSlipInfo;

    private String generatedPixCode;

    @FXML
    public void initialize() {
        paymentMethodComboBox.getItems().addAll(
                "Cartão de Crédito",
                "Cartão de Débito",
                "Pix",
                "Boleto",
                "Transferência Bancária"
        );

        paymentMethodComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                showRelevantPane(newVal);
            }
        });

        amountLabel.setText("Valor devido: R$450,75");

        // Gera código Pix aleatório
        generatedPixCode = generateRandomPixCode();
        pixCodeField.setText(generatedPixCode);

        // Carrega a imagem do QR Code
        Image qrImage = new Image(getClass().getResourceAsStream("/pix-qr.jpg"));
        pixQrCodeImage.setImage(qrImage);
    }

    private String generateRandomPixCode() {
        // Simula um código Pix copia e cola aleatório
        return "00020126" + UUID.randomUUID().toString().replace("-", "").substring(0, 20) + "BR.GOV.BCB.PIX" + new Random().nextInt(999999);
    }

    private void showRelevantPane(String method) {
        cardPane.setVisible(false);
        cardPane.setManaged(false);
        pixPane.setVisible(false);
        pixPane.setManaged(false);
        bankSlipPane.setVisible(false);
        bankSlipPane.setManaged(false);
        bankTransferPane.setVisible(false);
        bankTransferPane.setManaged(false);

        clearStatus();

        switch (method) {
            case "Cartão de Crédito":
            case "Cartão de Débito":
                cardPane.setVisible(true);
                cardPane.setManaged(true);
                break;
            case "Pix":
                pixPane.setVisible(true);
                pixPane.setManaged(true);
                showStatus("Aprovação é instantânea após pagar pelo app do seu banco.", Color.BLUE);
                break;
            case "Boleto":
                bankSlipPane.setVisible(true);
                bankSlipPane.setManaged(true);
                bankSlipInfo.setText("");
                break;
            case "Transferência Bancária":
                bankTransferPane.setVisible(true);
                bankTransferPane.setManaged(true);
                showStatus("Aguardando confirmação (pode levar até 2 dias úteis).", Color.ORANGE);
                break;
        }
    }

    @FXML
    private void handlePayWithCard() {
        if (cardNumberField.getText().isBlank() || cardHolderNameField.getText().isBlank()) {
            showStatus("Erro: Preencha todos os dados do cartão.", Color.RED);
            return;
        }

        showStatus("Processando Pagamento...", Color.ORANGE);

        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        pause.setOnFinished(event -> {
            showStatus("Pagamento aprovado!", Color.GREEN);
            cardHolderNameField.clear();
            cardNumberField.clear();
            expiryDateField.clear();
            cvvField.clear();
        });
        pause.play();
    }

    @FXML
    private void handleGenerateBankSlip() {
        Random rand = new Random();
        String barcode = String.format("12399.%05d 51234.%06d12 34567.%08d 4 %014d",
                rand.nextInt(99999), rand.nextInt(999999), rand.nextInt(99999999),
                System.currentTimeMillis() / 1000 + 1000000L);

        bankSlipInfo.setText("Boleto gerado! Linha digitável: " + barcode);
        System.out.println("Boleto gerado: " + barcode);

        showStatus("Aguardando pagamento do boleto (confirmação em até 2 dias úteis).", Color.ORANGE);
    }

    @FXML
    private void handleCopyPixCode() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(pixCodeField.getText());
        clipboard.setContent(content);
        showStatus("Código Pix copiado!", Color.GREEN);
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setTextFill(color);
    }

    private void clearStatus() {
        statusLabel.setText("");
    }
}