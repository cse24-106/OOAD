package com.felix.bankingsystem.view;

import com.felix.bankingsystem.model.*;
import com.felix.bankingsystem.controller.BankService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class OpenAccountController {

    @FXML private Label Available_balance;
    @FXML private Button Open_acc_btn;
    @FXML private ChoiceBox<String> accountTypeChoiceBox;
    @FXML private Button dashboard_btn;
    @FXML private Button deposit_btn;
    @FXML private Button open_acc_btn;
    @FXML private Button pers_det_btn;
    @FXML private Button transaction_hist_btn;
    @FXML private Button logout_btn;
    @FXML private Button withdraw_btn;

    private Customer customer;
    private BankService bankService;

    @FXML
    private void initialize() {
        setupAccountTypes();
        setupButtonActions();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        updateBalance();
    }

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    private void setupAccountTypes() {
        accountTypeChoiceBox.getItems().addAll("Savings Account", "Investment Account", "Cheque Account");
        accountTypeChoiceBox.setValue("Savings Account");
    }

    @FXML
    private void setupButtonActions() {
        dashboard_btn.setOnAction(e -> showDashboard());
        deposit_btn.setOnAction(e -> showDepositScreen());
        withdraw_btn.setOnAction(e -> showWithdrawScreen());
        open_acc_btn.setOnAction(e -> showOpenAccountScreen());
        pers_det_btn.setOnAction(e -> showPersonalDetailsScreen());
        transaction_hist_btn.setOnAction(e -> showTransactionHistoryScreen());
        Open_acc_btn.setOnAction(e -> handleOpenAccount());
        logout_btn.setOnAction(e -> logout());
    }

    private void updateBalance() {
        if (customer != null) {
            double totalBalance = customer.getAccounts().stream()
                    .mapToDouble(account -> account.getBalance())
                    .sum();
            Available_balance.setText(String.format("P%.2f", totalBalance));
        }
    }

    @FXML
    private void handleOpenAccount() {
        if (customer == null) {
            showAlert("Error", "No active customer session found!");
            return;
        }

        String accountType = accountTypeChoiceBox.getValue();

        if (accountType == null) {
            showAlert("Error", "Please select an account type");
            return;
        }

        try {
            switch (accountType) {
                case "Savings Account":
                    SavingsAccount savingsAccount = new SavingsAccount(
                            generateAccountNumber(), customer, 50.0);
                    customer.addAccount(savingsAccount);
                    bankService.openSavingsAccount(customer, generateAccountNumber(), 50.0);
                    break;

                case "Investment Account":
                    InvestmentAccount investmentAccount = new InvestmentAccount(
                                                generateAccountNumber(), customer, 500.0);
                    customer.addAccount(investmentAccount);
                    break;

                case "Cheque Account":
                    ChequeAccount chequeAccount = new ChequeAccount(
                            generateAccountNumber(), 0.0, customer,
                            "N/A", "N/A"); // TODO: You might want to collect company info
                    customer.addAccount(chequeAccount);
                    break;
            }

            showAlert("Success", accountType + " opened successfully!");
            updateBalance();

        } catch (Exception e) {
            showAlert("Error", "Failed to open account: " + e.getMessage());
            System.out.println("Failed to open account: " + e.getMessage());
        }
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
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
        // Already on open account screen
    }

    @FXML
    private void showPersonalDetailsScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/PersonalDetails.fxml", "Personal Details");
    }

    @FXML
    private void showTransactionHistoryScreen() {
        navigateTo("/com/felix/bankingsystem/FXML files/TransationHistory.fxml", "Transaction History");
    }

    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Set customer and bank service
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}