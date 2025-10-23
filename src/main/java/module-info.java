module com.felix.bankingsystem {
    requires javafx.controls;
    requires javafx.fxml;

    // Allow FXML files to access controller classes via reflection
    opens com.felix.bankingsystem.view to javafx.fxml;
    opens com.felix.bankingsystem.controller to javafx.fxml;
    opens com.felix.bankingsystem.model to javafx.base;

    // Export your packages that need to be used outside the module
    exports com.felix.bankingsystem.main;
    exports com.felix.bankingsystem.controller;
    exports com.felix.bankingsystem.model;
    exports com.felix.bankingsystem.view;
}
