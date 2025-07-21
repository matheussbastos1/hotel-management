module org.example.hotelmanagement {
// Módulos JavaFX necessários
    requires javafx.controls;
    requires javafx.fxml;

    // Dependência do Lombok (se você usa)
    requires static lombok;
    requires itextpdf;
    requires java.sql;

    // --- PERMISSÕES DE ACESSO (Onde está a correção) ---

    // Permite que o FXML acesse as classes nos seus controllers
    opens com.example.hotelmanagement.view.controllers to javafx.fxml;

    // **LINHAS NOVAS E CRÍTICAS PARA A TABELA FUNCIONAR**
    // Permite que a TableView acesse (via "reflection") suas classes no pacote 'models'
    opens com.example.hotelmanagement.models to javafx.base;
    // Permite que a TableView classes no pacote 'dto'
    opens com.example.hotelmanagement.dto to javafx.base;


    // --- PACOTES EXPORTADOS ---
    exports com.example.hotelmanagement;
    exports com.example.hotelmanagement.view;
    exports com.example.hotelmanagement.view.controllers;
}