package com.example.hotelmanagement;

import javafx.application.Application;
import javafx.stage.Stage;

public class HotelManagementApplication extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Sistema de Gerenciamento de Hotel");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}