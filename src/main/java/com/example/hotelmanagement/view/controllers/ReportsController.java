package com.example.hotelmanagement.view.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ReportsController {

    /**
     * Chamado pelo botão "Relatório de Estadias".
     * Abre a tela específica de estadias.
     */

    @FXML
    private void handleOpenEstadiaReport(ActionEvent event) {
        // Supondo que o nome do seu arquivo seja StayReport.fxml
        loadReportScreen("/StayView.fxml", "Relatório de Estadias", event);
    }
    @FXML
    private void handleOpenFinancialReport(ActionEvent event) {
        // Certifique-se de que o nome do arquivo FXML está correto
        loadReportScreen("/FinancialReportView.fxml", "Relatório Financeiro", event);
    }

    // Você pode adicionar outros métodos para os outros botões aqui no futuro...

    /**
     * Chamado pelo botão "Voltar ao Dashboard".
     */
    @FXML
    private void handleVoltar(ActionEvent event) {
        loadReportScreen("/DashboardForm.fxml", "Dashboard Principal", event);
    }

    /**
     * Método auxiliar para carregar as diferentes telas de relatório.
     */
    private void loadReportScreen(String fxmlFile, String title, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException | NullPointerException e) {
            System.err.println("ERRO AO CARREGAR TELA DE RELATÓRIO: " + fxmlFile);
            e.printStackTrace();
        }
    }
}