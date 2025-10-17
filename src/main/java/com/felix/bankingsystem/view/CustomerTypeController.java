package com.felix.bankingsystem.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerTypeController {

    @FXML
    private Button Individual_btn;

    @FXML
    private Button Organisation_btn;

    @FXML
    private void initialize() {
        // Set up button actions
        Individual_btn.setOnAction(e -> handleIndividualCustomer());
        Organisation_btn.setOnAction(e -> handleOrganisationCustomer());
    }

    private void handleIndividualCustomer() {
        try {
            // Load individual customer registration screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/view/CustomerSignup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) Individual_btn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Individual Customer Registration");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleOrganisationCustomer() {
        try {
            // Load organisation registration screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/view/OrganisationSignup.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) Organisation_btn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Organisation Registration");
            stage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
