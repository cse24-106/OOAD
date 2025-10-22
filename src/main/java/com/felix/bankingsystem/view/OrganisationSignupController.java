package com.felix.bankingsystem.view;

import com.felix.bankingsystem.model.CompanyCustomer;
import com.felix.bankingsystem.controller.BankService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class OrganisationSignupController {

    @FXML
    private TextField Address_txt;

    @FXML
    private TextField Contact_person_txt1;

    @FXML
    private Label CustomerID;

    @FXML
    private TextField Orgname_txt;

    @FXML
    private Label Phonenum_txt;

    @FXML
    private TextField Registrationnum_txt;

    @FXML
    private TextField number_txt1;

    @FXML
    private Button signup_btn;

    @FXML
    private TextField incoporation_date;

    @FXML
    private TextField tax_ID_txt;

    private BankService bankService;

    @FXML
    private void initialize() {
        String customerId = generateCustomerId();
        CustomerID.setText(customerId);

        // Set up signup button action
        signup_btn.setOnAction(e -> handleSignup());
    }

    private String generateCustomerId() {
        // Generate a unique customer ID for organization
        return "ORG" + System.currentTimeMillis();
    }

    private void handleSignup() {
        String companyName = Orgname_txt.getText().trim();
        String registrationNumber = Registrationnum_txt.getText().trim();
        String address = Address_txt.getText().trim();
        String contactPerson = Contact_person_txt1.getText().trim();
        String phone = number_txt1.getText().trim();
        String customerId = CustomerID.getText();
        String tax_ID = tax_ID_txt.getText();
        String incoporation_Date = incoporation_date.getText();

        // Validate input
        if (!validateInput(companyName, registrationNumber, address, contactPerson, phone, tax_ID, incoporation_Date)) {
            return;
        }

        try {
            // Create new company customer
            CompanyCustomer company = new CompanyCustomer(customerId, companyName, registrationNumber, address, contactPerson, phone, tax_ID, incoporation_Date);

            // Add company to bank service
            if (bankService != null) {
                bankService.addCustomer(company);
            }

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Organization registered successfully!\nCustomer ID: " + customerId);

            // Return to login screen
            returnToLogin();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to register organization: " + e.getMessage());
        }
    }

    private boolean validateInput(String orgName, String regNumber, String address,
                                  String contactPerson, String phone, String tax_ID, String incoporation_Date) {
        if (orgName.isEmpty() || regNumber.isEmpty() || address.isEmpty() ||
                contactPerson.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Please fill in all fields");
            return false;
        }

        return true;
    }

    private void returnToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) signup_btn.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Capital Bank - Login");
            stage.centerOnScreen();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Cannot return to login screen: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
