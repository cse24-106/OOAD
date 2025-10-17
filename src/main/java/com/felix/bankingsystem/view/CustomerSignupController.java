package com.felix.bankingsystem.view;

import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.controller.BankService ;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class CustomerSignupController {

    @FXML
    private Label CustomerID;

    @FXML
    private AnchorPane CustomerRegistraion_screen;

    @FXML
    private AnchorPane IndividualRegistration;

    @FXML
    private TextField address_txt;

    @FXML
    private TextField email_txt;

    @FXML
    private AnchorPane employee_Info;

    @FXML
    private TextField firstname_txt;

    @FXML
    private TextField nationalID_txt;

    @FXML
    private TextField number_txt;

    @FXML
    private Button signup_btn;

    @FXML
    private TextField surname_txt;

    private BankService bankService;

    @FXML
    private void initialize() {
        // Generate a customer ID
        String customerId = generateCustomerId();
        CustomerID.setText(customerId);

        // Set up signup button action
        signup_btn.setOnAction(e -> handleSignup());
    }

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    private String generateCustomerId() {
        return "CUST" + System.currentTimeMillis();
    }

    private void handleSignup() {
        String firstName = firstname_txt.getText().trim();
        String surname = surname_txt.getText().trim();
        String nationalId = nationalID_txt.getText().trim();
        String address = address_txt.getText().trim();
        String phone = number_txt.getText().trim();
        String email = email_txt.getText().trim();
        String customerId = CustomerID.getText();

        // Validate input
        if (!validateInput(firstName, surname, nationalId, address, phone, email)) {
            return;
        }

        try {
            // Create new individual customer
            Customer customer = new Customer(customerId, firstName, surname, address, email, phone);
            customer.setNationalID(nationalId);

            // Add customer to bank service
            if (bankService != null) {
                bankService.addCustomer(customer);
            }

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Individual customer registered successfully!\nCustomer ID: " + customerId);

            // Return to login screen
            returnToLogin();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to register customer: " + e.getMessage());
        }
    }

    private boolean validateInput(String firstName, String surname, String nationalId,
                                  String address, String phone, String email) {
        if (firstName.isEmpty() || surname.isEmpty() || nationalId.isEmpty() ||
                address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Please fill in all fields");
            return false;
        }

        // Basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Please enter a valid email address");
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

