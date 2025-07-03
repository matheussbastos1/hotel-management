package com.example.hotelmanagement.view.controllers;

import com.example.hotelmanagement.repository.GuestRepository;
import com.example.hotelmanagement.repository.impl.GuestRepositoryImpl;
import com.example.hotelmanagement.view.GuestRegistrationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GuestRegistrationApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Usando caminho relativo mais simples
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GuestRegistrationForm.fxml"));
        Parent root = loader.load();

        GuestRegistrationController controller = loader.getController();
        GuestRepository guestRepository = new GuestRepositoryImpl();
        controller.setGuestRepository(guestRepository);

        primaryStage.setTitle("Cadastro de Hóspedes");
        primaryStage.setScene(new Scene(root, 400, 350));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}