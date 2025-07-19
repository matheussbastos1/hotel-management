package com.example.hotelmanagement;

import javafx.application.Application;
import javafx.stage.Stage;

public class HotelManagementApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Hotel Management System");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);

        stage.show();

    }
}
