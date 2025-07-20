package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.controller.RoomController;
import com.example.hotelmanagement.repository.impl.RoomRepositoryImpl;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DashboardController {

    // --- Componentes FXML ---
    @FXML private Label dateTimeLabel;
    @FXML private Label occupiedRoomsLabel;
    @FXML private Label guestCountLabel;
    @FXML private Label occupancyRateLabel;
    @FXML private PieChart roomStatusChart;
    @FXML private ProgressBar occupancyProgressBar;
    @FXML private Label occupancyProgressLabel;

    @FXML
    public void initialize() {
        startClock();
        loadDashboardData();
    }

    private void startClock() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            dateTimeLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


    private void loadDashboardData() {
        // --- DADOS SIMULADOS (VOCÊ PODE MUDAR ESSES NÚMEROS) ---
        int occupiedRooms = 45;
        int availableRooms = 20;
        int maintenanceRooms = 5;
        int totalHotelRooms = occupiedRooms + availableRooms + maintenanceRooms;
        int currentGuests = 110;
        final int MAX_GUEST_CAPACITY = 350;

        // --- ATUALIZA OS CARDS ---
        occupiedRoomsLabel.setText(String.valueOf(occupiedRooms));
        guestCountLabel.setText(String.valueOf(currentGuests));
        occupancyRateLabel.setText(String.format("%.0f%%", (double) occupiedRooms / totalHotelRooms * 100));

        // --- ATUALIZA O GRÁFICO DE PIZZA ---
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Ocupados (" + occupiedRooms + ")", occupiedRooms),
                        new PieChart.Data("Livres (" + availableRooms + ")", availableRooms),
                        new PieChart.Data("Manutenção (" + maintenanceRooms + ")", maintenanceRooms));
        roomStatusChart.setData(pieChartData);
        roomStatusChart.setClockwise(false); // Efeito visual

        // --- ATUALIZA O MEDIDOR DE OCUPAÇÃO TOTAL ---
        double occupancyProgress = (double) currentGuests / MAX_GUEST_CAPACITY;
        occupancyProgressBar.setProgress(occupancyProgress);
        occupancyProgressLabel.setText(String.format("%d / %d hóspedes", currentGuests, MAX_GUEST_CAPACITY));
    }

    // --- Métodos de Navegação (mantidos como antes) ---
    @FXML private void handleAbrirCadastroGeral(ActionEvent event) { loadScreen("/GeneralReserve.fxml", "Cadastro Geral", event); }
    // Este método deve estar no seu DashboardController.java
    @FXML
    private void handleAbrirFormularioQuarto(ActionEvent event) {
        try {
            // 1. Carrega o FXML do formulário
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RoomForm.fxml")); // Verifique se o caminho está certo
            Parent root = loader.load();

            // 2. Pega a instância do controller do formulário ANTES de mostrar a tela
            RoomFormController formController = loader.getController();

            // 3. CRIA o RoomController que estava faltando


           // Adicione antes de setRoomController
         RoomController roomController = new RoomController(new RoomRepositoryImpl());
           formController.setRoomController(roomController);

            // 5. Agora sim, mostra a tela, já com tudo conectado
            Stage stage = new Stage();
            stage.setTitle("Adicionar Novo Quarto");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }    @FXML private void handleAbrirGestaoServicos(ActionEvent event) { loadScreen("/ServicesForm.fxml", "Gestão de Serviços", event); }
    @FXML private void handleAbrirRelatorios(ActionEvent event) { loadScreen("/ReportsForm.fxml", "Relatórios", event); }
    @FXML private void handleAbrirPagamentos(ActionEvent event) { loadScreen("/payment-screen.fxml", "Pagamentos", event); }
    // Dentro da classe DashboardController.java

    @FXML
    private void handleAbrirCheckOut(ActionEvent event) {
        try {
            // Carrega o arquivo FXML da tela de check-out
            Parent checkOutRoot = FXMLLoader.load(getClass().getResource("/CheckOut.fxml"));

            // Pega a janela atual (o Stage) a partir do evento do clique
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Cria uma nova cena com o conteúdo da tela de check-out
            Scene scene = new Scene(checkOutRoot);

            // Define a nova cena na janela
            stage.setScene(scene);

            // Atualiza o título da janela
            stage.setTitle("Tela de Check-out");

            // Mostra a janela atualizada
            stage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de check-out:");
            e.printStackTrace();
            // Opcional: mostrar um alerta de erro para o usuário
        }
    }
    @FXML
    private void handleAbrirCheckIn(ActionEvent event) { // ou o nome que você usa
        try {
            // Carrega a nova tela de painel de check-in
            Parent checkInRoot = FXMLLoader.load(getClass().getResource("/CheckInView.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(checkInRoot));
            stage.setTitle("Painel de Check-in");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        }
    }
}