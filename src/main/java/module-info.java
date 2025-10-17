module com.felix.bankingsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.naming;


    opens com.felix.bankingsystem to javafx.fxml;
    exports com.felix.bankingsystem;
    exports com.felix.bankingsystem.model;
    opens com.felix.bankingsystem.model to javafx.fxml;
}