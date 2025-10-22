package com.felix.bankingsystem.view;

import com.felix.bankingsystem.controller.Authenticator;
import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.controller.BankService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

        @FXML
        private AnchorPane Login_pic;

        @FXML
        private Label error_message;

        @FXML
        private Button login_btn;

        @FXML
        private BorderPane login_screen;

        @FXML
        private TextField password_txt;

        @FXML
        private Hyperlink signup_hyper;

        @FXML
        private TextField username_txt;

        private BankService bankService;

        @FXML
        private void initialize() {
                //Sets initial focus
                username_txt.requestFocus();
                error_message.setVisible(false);

                bankService = new BankService();

                password_txt.setOnAction(event -> {
                    try {
                        handlePasswordEnter(event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
        }

        @FXML
        private void handleLogin(ActionEvent event) throws IOException {
                String username = username_txt.getText();
                String password = password_txt.getText();

                if(!validateInput(username, password)) {
                        return;
                }

                try{
                        Customer customer =Authenticator(username, password);
                        System.out.println("Login successful for: " + username);
                        loadDashboard(customer);
                } catch (Exception e) {
                        System.err.println("Login failed: " + e.getMessage());
                        showError("Invalid username or password");
                }
        }

        private boolean validateInput(String username, String password) {
                if(username.isEmpty() || password.isEmpty()) {
                        showError("Please fill in all fields");
                        return false;
                }
                return true;
        }

        @FXML
        private void handlePasswordEnter(ActionEvent event) throws IOException {
                handleLogin(event);
        }

        @FXML
        private void handleSignup(ActionEvent event) {
                try {
                        loadCustomerTypeView();
                } catch (IOException e) {
                        showError("Cannot load signup page: " + e.getMessage());
                }
        }

        private void loadCustomerTypeView() throws IOException {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/view/CustomerType.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) signup_hyper.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Choose Customer Type");
                stage.centerOnScreen();
        }

        private void showError(String message) {
                error_message.setText(message);
                error_message.setVisible(true);
        }

        private void loadDashboard(Customer customer) {
                try {
                        // Load the dashboard FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("com/felix/bankingsystem/FXML files/Dashboard.fxml"));
                        Parent root = loader.load();

                        DashboardController dashboardController = loader.getController();
                        dashboardController.setCustomer(customer);
                        dashboardController.setBankService(bankService);

                        // Get the current stage
                        Stage stage = (Stage) login_btn.getScene().getWindow();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.setTitle("Capital Bank - Dashboard");
                        stage.centerOnScreen();

                } catch (IOException e) {
                        showError("Error loading dashboard: " + e.getMessage());
                        e.printStackTrace();
                }
        }

        private void loadSignupView() throws IOException {
                // Load the signup FXML
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/CustomerType.fxml"));
                Parent root = loader.load();

                CustomerTypeController signupController = loader.getController();
                signupController.setBankService(bankService);

                Stage stage = (Stage) signup_hyper.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Capital Bank - Sign Up");
                stage.centerOnScreen();
        }
}
