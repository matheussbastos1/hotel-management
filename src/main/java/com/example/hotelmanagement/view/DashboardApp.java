package com.example.hotelmanagement.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardForm.fxml"));
            Parent root = loader.load();


            Scene scene = new Scene(root);


            primaryStage.setTitle("Sistema de Gestão Hoteleira");


            primaryStage.setScene(scene);


            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static void main(String[] args) {

        launch(args);
    }

}
