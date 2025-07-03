module org.example.hotelmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    exports com.example.hotelmanagement;
    opens com.example.hotelmanagement to javafx.fxml;

    exports com.example.hotelmanagement.view.controller;
    opens com.example.hotelmanagement.view.controllers to javafx.fxml;
}