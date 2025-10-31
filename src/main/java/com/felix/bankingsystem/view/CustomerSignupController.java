package com.felix.bankingsystem.view;

import com.felix.bankingsystem.controller.BankService ;
import com.felix.bankingsystem.model.IndividualCustomer;
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

public class CustomerSignupController {

    @FXML
    private Label CustomerID;

    @FXML
    private TextField address_txt;

    @FXML
    private TextField email_txt;

    @FXML
    private TextField firstname_txt;

    @FXML
    private TextField nationalID_txt;

    @FXML
    private TextField number_txt;

    @FXML
    private TextField password_txt;

    @FXML
    private Button signup_btn;

    @FXML
    private TextField surname_txt;

    @FXML
    private TextField source_of_funds_txt;

    @FXML
    private Button return_to_login_btn;

    private BankService bankService;

    @FXML
    private void initialize() {
        // Generate a customer ID
        String customerId = generateCustomerId();
        CustomerID.setText(customerId);

        // Set up signup button action
        signup_btn.setOnAction(event -> handleSignup());
        return_to_login_btn.setOnAction(e -> returnToLogin());
    }

    private String generateCustomerId() {
        return "CUST" + System.currentTimeMillis();
    }

    @FXML
    private void handleSignup() {
        String firstName = firstname_txt.getText().trim();
        String surname = surname_txt.getText().trim();
        String nationalId = nationalID_txt.getText().trim();
        String address = address_txt.getText().trim();
        String phone = number_txt.getText().trim();
        String email = email_txt.getText().trim();
        String customerId = CustomerID.getText();
        String source_of_funds = source_of_funds_txt.getText();
        String password = password_txt.getText().trim();

        // Validate input
        if (!validateInput(firstName, surname, nationalId, address, phone, email, source_of_funds)) {
            return;
        }

        try {
            // Create new individual customer
            IndividualCustomer customer = new IndividualCustomer(customerId, firstName, surname, address, phone, email, source_of_funds);
            customer.setNationalID(nationalId);
            customer.setPassword(password_txt.getText());

            // Add customer to bank service
            if (bankService != null) {
                bankService.addCustomer(customer);
            }

            System.out.println("Sent to bank service");
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Individual customer registered successfully!\nCustomer ID: " + customerId);

            // Return to log in screen
            returnToLogin();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Failed to register customer: " + e.getMessage());
        }
    }

    private boolean validateInput(String firstName, String surname, String nationalId,
                                  String address, String phone, String email, String source_of_funds) {
        if (firstName.isEmpty() || surname.isEmpty() || nationalId.isEmpty() ||
                address.isEmpty() || phone.isEmpty() || email.isEmpty() || source_of_funds.isEmpty()) {
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

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    @FXML
    private void returnToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/LoginView.fxml"));
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

