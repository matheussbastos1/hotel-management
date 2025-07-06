package com.example.hotelmanagement.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class StayTestApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carrega o FXML da tela de estadia
            URL fxmlLocation = getClass().getResource("/StayView.fxml");
            if (fxmlLocation == null) {
                System.err.println("Erro: StayView.fxml não encontrado. Verifique o caminho.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlLocation);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Gerenciamento de Estadias (Teste)");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            System.err.println("Erro ao carregar a tela de estadia: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}