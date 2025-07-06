package com.example.hotelmanagement.view.controllers;



import com.example.hotelmanagement.controller.RoomController;
import com.example.hotelmanagement.repository.impl.RoomRepositoryImpl;
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


    @FXML
    private void handleAbrirCadastroGeral(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GeneralReserve.fxml"));
            Parent cadastroRoot = loader.load();
            ReservaController reservaController = loader.getController();
            // Use a mesma instância de RoomController
            reservaController.setRoomController(new RoomController(new RoomRepositoryImpl()));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(cadastroRoot));
            stage.setTitle("Cadastro Geral");
            stage.show();
        } catch (IOException e) {
            System.err.println("FALHA AO CARREGAR A TELA DE CADASTRO:");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAbrirRelatorios(ActionEvent event) {
        try {
            Parent reportsRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ReportsForm.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(reportsRoot));
            stage.setTitle("Relatórios");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleAbrirCadastroQuarto(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RoomForm.fxml"));
            Parent root = loader.load();
            RoomFormController controller = loader.getController();
            controller.setRoomController(new RoomController(new RoomRepositoryImpl()));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestão de Quartos");
            stage.show();
        } catch (IOException e) {
            System.err.println("ERRO: Falha ao carregar a tela de Quartos.");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleAbrirGestaoServicos(ActionEvent event) {
        try {
            // !! Use o nome exato do seu arquivo FXML de serviços !!
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ServicesForm.fxml")));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gestão de Serviços");
            stage.show();
        } catch (IOException e) {
            System.err.println("ERRO: Falha ao carregar a tela de Serviços.");
            e.printStackTrace();
        }
    }




    // Você pode adicionar outros métodos para outros botões aqui...
    // @FXML
    // private void handleAbrirTelaDeQuartos(ActionEvent event) { ... }
}