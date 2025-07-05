// Caminho: src/com/example/hotelmanagement/view/ServicesApp.java
package com.example.hotelmanagement.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ServicesApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Carrega o novo arquivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ServicesForm.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Services Module");
        primaryStage.setScene(new Scene(root, 900, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}