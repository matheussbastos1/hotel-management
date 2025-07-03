module org.example.hotelmanagement {
    // remova a linha "requires lombok;"

    // Adicione os módulos necessários para JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok; // use 'requires static' para dependências de compilação

    // Abra pacotes necessários para o JavaFX
    opens com.example.hotelmanagement to javafx.fxml;
    opens com.example.hotelmanagement.view to javafx.fxml;

    // Exporte os pacotes que você quer tornar visíveis
    exports com.example.hotelmanagement;
    exports com.example.hotelmanagement.view;
    exports com.example.hotelmanagement.view.controllers;
    opens com.example.hotelmanagement.view.controllers to javafx.fxml;
}