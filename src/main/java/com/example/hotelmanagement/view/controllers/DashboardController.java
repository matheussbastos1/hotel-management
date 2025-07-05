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


    @FXML
    private void handleAbrirCadastroGeral(ActionEvent event) {
        try {
            // Carrega o arquivo FXML da tela de cadastro.
            // MUDE O NOME "ReservaView.fxml" se o seu arquivo tiver outro nome.
            Parent cadastroRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GeneralReserve.fxml")));

            // Pega a janela (Stage) atual a partir do botão que foi clicado.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Coloca a nova tela na janela.
            stage.setScene(new Scene(cadastroRoot));
            stage.setTitle("Cadastro Geral"); // Muda o título da janela
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
    private void handleAbrirTelaDeReservas(ActionEvent event) {
        try {
            // 1. Carrega o arquivo FXML da tela que você quer abrir.
            //    !! MUITA ATENÇÃO AQUI !!
            //    O nome "/GuestRegistrationForm.fxml" deve ser EXATAMENTE igual ao seu arquivo na pasta 'resources'.
            Parent telaDeReservasRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/GeneralReserve.fxml")));

            // 2. Pega a janela (Stage) atual a partir do botão que foi clicado.
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 3. Cria uma nova cena com a tela de reserva.
            Scene scene = new Scene(telaDeReservasRoot);

            // 4. Define a nova cena na janela, efetivamente "trocando de tela".
            stage.setScene(scene);
            stage.setTitle("Nova Reserva"); // Opcional: muda o título da janela
            stage.show(); // Garante que a janela seja exibida

        } catch (IOException | NullPointerException e) {
            System.err.println("ERRO CRÍTICO AO NAVEGAR: Não foi possível carregar o arquivo FXML da tela de reserva.");
            System.err.println("Verifique se o nome do arquivo em 'getResource()' está correto e se o arquivo está na pasta 'resources'.");
            e.printStackTrace();
            // Aqui seria um bom lugar para mostrar um Alert de erro para o usuário.
        }
    }

    // Você pode adicionar outros métodos para outros botões aqui...
    // @FXML
    // private void handleAbrirTelaDeQuartos(ActionEvent event) { ... }
}