package com.example.hotelmanagement;

import javafx.application.Application;
import javafx.stage.Stage;

public class HotelManagementApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        // Usando caminho relativo mais simples
        stage.setTitle("Hotel Management System");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);

        // Carregar a interface gráfica principal
        // FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
        // Parent root = loader.load();
        // stage.setScene(new Scene(root));

        // Exibir a janela
        stage.show();

    }



}
