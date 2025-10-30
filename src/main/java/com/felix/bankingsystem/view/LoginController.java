package com.felix.bankingsystem.view;

import com.felix.bankingsystem.controller.Authenticator;
import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.controller.BankService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
        private PasswordField password_txt;

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
                String identifier = username_txt.getText();
                String password = password_txt.getText();

                if(!validateInput(identifier, password)) {
                        return;
                }

                try {
                        Authenticator auth = new Authenticator(identifier, password, bankService);
                        Customer customer = auth.login();

                        if (customer != null) {
                                System.out.println("Login successful for " + customer.getDisplayName());
                                loadDashboard(customer)
                        } else {
                                System.out.println("Invalid credentials");
                                showError("Invalid credentials");
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        private boolean validateInput(String identifier, String password) {
                if(identifier.isEmpty() || password.isEmpty()) {
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/CustomerType.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) signup_hyper.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setTitle("Choose Customer Type");
                stage.centerOnScreen();
        }

//        private void loadDashboard() throws IOException {
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/Dashboard.fxml"));
//                Parent root = loader.load();
//
//                Stage stage = (Stage) signup_hyper.getScene().getWindow();
//                Scene scene = new Scene(root);
//                stage.setScene(scene);
//                stage.setTitle("Choose Customer Type");
//                stage.centerOnScreen();
//        }

        private void showError(String message) {
                error_message.setText(message);
                error_message.setVisible(true);
        }
}