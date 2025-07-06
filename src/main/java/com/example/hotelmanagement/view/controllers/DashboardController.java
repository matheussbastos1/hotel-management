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

public class DashboardController {

    /**
     * Abre a tela de Cadastro Geral (Reservas).
     */
    @FXML
    private void handleAbrirCadastroGeral(ActionEvent event) {
        // A forma simplificada: apenas carregue a tela.
        // A própria ReservaController já sabe como se inicializar.
        loadScreen("/GeneralReserve.fxml", "Cadastro Geral", event);
    }

    /**
     * Abre a tela de Cadastro de Quartos.
     */
    @FXML
    private void handleAbrirCadastroQuarto(ActionEvent event) {
        // A forma simplificada: apenas carregue a tela.
        // A própria RoomFormController já sabe como se inicializar.
        loadScreen("/RoomForm.fxml", "Gestão de Quartos", event);
    }

    /**
     * Abre a tela de Relatórios.
     */
    @FXML
    private void handleAbrirRelatorios(ActionEvent event) {
        loadScreen("/ReportsForm.fxml", "Relatórios", event);
    }

    /**
     * Abre a tela de Gestão de Serviços.
     */
    @FXML
    private void handleAbrirGestaoServicos(ActionEvent event) {
        loadScreen("/ServicesForm.fxml", "Gestão de Serviços", event);
    }

    /**
     * Método auxiliar genérico para carregar qualquer tela.
     * Evita repetição de código.
     * @param fxmlFile O nome do arquivo FXML a ser carregado (ex: "/NomeDaTela.fxml")
     * @param title O título para a nova janela.
     * @param event O evento de clique que originou a chamada.
     */
    private void loadScreen(String fxmlFile, String title, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlFile)));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException | NullPointerException e) {
            System.err.println("ERRO AO CARREGAR TELA: " + fxmlFile);
            e.printStackTrace();
            // Seria bom mostrar um Alert de erro aqui.
        }
    }
}