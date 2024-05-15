module vagas.portalvagascliente {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.xml;

    opens cliente to javafx.fxml;
    opens cliente.controllers to javafx.fxml;
    exports cliente;
    exports cliente.controllers;
}