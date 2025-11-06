package com.felix.bankingsystem.view;

import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.model.Account;
import com.felix.bankingsystem.model.Transaction;
import com.felix.bankingsystem.controller.BankService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDateTime;


public class WithdrawController {

    @FXML
    private Label Available_balance;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button deposit_btn;

    @FXML
    private Button logout_btn;

    @FXML
    private Button open_acc_btn;

    @FXML
    private Button pers_det_btn;

    @FXML
    private Button transaction_hist_btn;

    @FXML
    private TextField withdraw_amount;

    @FXML
    private Button withdraw_btn;

    @FXML
    private Button Withdraw_btn;

    private Customer customer;
    private BankService bankService;
    private Account selectedAccount;

    @FXML
    private void initialize() {
        setupButtonActions();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        updateView();
    }

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    @FXML
    private void setupButtonActions() {
        dashboard_btn.setOnAction(e -> showDashboard());
        deposit_btn.setOnAction(e -> showDepositScreen());
        withdraw_btn.setOnAction(e -> showWithdrawScreen());
        open_acc_btn.setOnAction(e -> showOpenAccountScreen());
        pers_det_btn.setOnAction(e -> showPersonalDetailsScreen());
        transaction_hist_btn.setOnAction(e -> showTransactionHistoryScreen());
        logout_btn.setOnAction(e -> logout());
        Withdraw_btn.setOnAction(e -> handleWithdraw());
    }

    private void updateView() {
        if (customer != null) {
            double totalBalance = customer.getAccounts().stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            Available_balance.setText(String.format("P%.2f", totalBalance));
        }
    }

    @FXML
    private void handleWithdraw() {
        try {
            double amount = Double.parseDouble(withdraw_amount.getText().trim());

            if (amount <= 0) {
                showAlert("Error", "Amount must be greater than 0");
                return;
            }

            // For simplicity, withdraw from first account
            if (!customer.getAccounts().isEmpty()) {
                selectedAccount = customer.getAccounts().get(0);

                // Check sufficient balance
                if (selectedAccount.getBalance() < amount) {
                    showAlert("Error", "Insufficient funds");
                    return;
                }

                // Create transaction
                Transaction transaction = new Transaction(
                        "T" + System.currentTimeMillis(),
                        "WITHDRAWAL",
                        amount,
                        selectedAccount.getBalance() - amount
                );

                // Update account balance
                bankService.withdraw(customer, amount);
                updateView();

                showAlert("Success", String.format("Withdrawn P%.2f successfully", amount));
                withdraw_amount.clear();
                updateView();

            } else {
                showAlert("Error", "No accounts available for withdrawal");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        }
    }

    @FXML
    private void showDashboard() {
        navigateTo("/com/felix/bankingsystem/FXML files/Dashboard.fxml", "Dashboard");
    }

    @FXML
    private void showDepositScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/Deposit.fxml", "Deposit Funds");
    }

    @FXML
    private void showWithdrawScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/Withdraw.fxml", "Withdraw Funds");
    }

    @FXML
    private void showOpenAccountScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/Openaccount.fxml", "Open Account");
    }

    @FXML
    private void showPersonalDetailsScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/Personaldetails.fxml", "Personal Details");
    }

    @FXML
    private void showTransactionHistoryScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/TransactionHistory.fxml", "Transaction History");
    }

    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logout_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Capital Bank - Login");
            stage.centerOnScreen();

        } catch (IOException e) {
            showAlert("Error", "Cannot logout: " + e.getMessage());
        }
    }

    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Object controller = loader.getController();
            setControllerProperties(controller);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);

        } catch (IOException e) {
            showAlert("Error", "Cannot navigate: " + e.getMessage());
        }
    }

    private void setControllerProperties(Object controller) {
        if (controller instanceof DashboardController) {
            ((DashboardController) controller).setCustomer(customer);
            ((DashboardController) controller).setBankService(bankService);
        } else if (controller instanceof DepositController) {
            ((DepositController) controller).setCustomer(customer);
            ((DepositController) controller).setBankService(bankService);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
