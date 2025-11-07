package com.felix.bankingsystem.view;

import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.model.Account;
import com.felix.bankingsystem.controller.BankService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

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

    @FXML
    private TextField account_num_txt;

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
            String accountNumber = account_num_txt.getText().trim();
            if (accountNumber.isEmpty()) {
                showAlert("Error", "Please enter the account number.");
                return;
            }

            double amount = Double.parseDouble(withdraw_amount.getText().trim());
            if (amount <= 0) {
                showAlert("Error", "Amount must be greater than 0");
                return;
            }

            Account selectedAccount = customer.getAccounts().stream()
                    .filter(acc -> acc.getAccountNumber().equalsIgnoreCase(accountNumber))
                    .findFirst()
                    .orElse(null);

            if (selectedAccount == null) {
                showAlert("Error", "Account number not found.");
                return;
            }

            if (selectedAccount.getBalance() < amount) {
                showAlert("Error", "Insufficient funds in this account.");
                return;
            }

            bankService.withdrawFromAccount(customer, selectedAccount, amount);

            updateView();
            bankService.saveAllData();

            showAlert("Success", String.format("Withdrawn P%.2f from account %s successfully.",
                    amount, selectedAccount.getAccountNumber()));

            // Clear inputs
            withdraw_amount.clear();
            account_num_txt.clear();

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        } catch (Exception e) {
            showAlert("Error", "Withdrawal failed: " + e.getMessage());
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
        navigateTo("/com/felix/bankingsystem/FXML files/OpenAccount.fxml", "Open Account");
    }

    @FXML
    private void showPersonalDetailsScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/PersonalDetails.fxml", "Personal Details");
    }

    @FXML
    private void showTransactionHistoryScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/TransationHistory.fxml", "Transaction History");
    }

    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/LoginView.fxml"));
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
        } else if (controller instanceof WithdrawController) {
            ((WithdrawController) controller).setCustomer(customer);
            ((WithdrawController) controller).setBankService(bankService);
        } else if (controller instanceof TransactionHistoryController) {
            ((TransactionHistoryController) controller).setCustomer(customer);
            ((TransactionHistoryController) controller).setBankService(bankService);
        } else if (controller instanceof OpenAccountController) {
            ((OpenAccountController) controller).setCustomer(customer);
            ((OpenAccountController) controller).setBankService(bankService);
        }  else if (controller instanceof PersonalDetailsController) {
            ((PersonalDetailsController) controller).setCustomer(customer);
            ((PersonalDetailsController) controller).setBankService(bankService);
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
