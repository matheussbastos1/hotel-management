package com.example.hotelmanagement.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class PaymentView extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlLocation = getClass().getResource("/payment-screen.fxml");
        if (fxmlLocation == null) {
            System.err.println("Could not find FXML file. Please check the path.");
            return;
        }

        Parent root = FXMLLoader.load(fxmlLocation);

        // Define o tamanho da cena para 700x600
        Scene scene = new Scene(root, 700, 600);

        primaryStage.setTitle("Payment Module");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}