package com.example.hotelmanagement.view.controllers; // Ou um novo pacote para testes de UI

import com.example.hotelmanagement.controller.RoomController;
import com.example.hotelmanagement.repository.impl.RoomRepositoryImpl;
import com.example.hotelmanagement.view.RoomFormController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class RoomApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RoomForm.fxml"));
        Parent root = loader.load();

        RoomFormController controller = loader.getController();

        // Crie as instâncias do repositório e do controlador
        RoomRepositoryImpl roomRepository = new RoomRepositoryImpl();
        RoomController roomController = new RoomController(roomRepository);

        // Injete o controlador no RoomFormController
        controller.setRoomController(roomController);

        primaryStage.setTitle("Cadastro de Quartos");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}