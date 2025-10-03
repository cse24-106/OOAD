module com.felix.bankingsystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.felix.bankingsystem to javafx.fxml;
    exports com.felix.bankingsystem;
}