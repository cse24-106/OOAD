package com.felix.bankingsystem.view;

import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.model.SavingsAccount;
import com.felix.bankingsystem.model.InvestmentAccount;
import com.felix.bankingsystem.model.ChequeAccount;
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

    private void setupButtonActions() {
        dashboard_btn.setOnAction(e -> showDashboard());
        deposit_btn.setOnAction(e -> showDepositScreen());
        withdraw_btn.setOnAction(e -> showWithdrawScreen());
        open_acc_btn.setOnAction(e -> showOpenAccountScreen());
        pers_det_btn.setOnAction(e -> showPersonalDetailsScreen());
        transaction_hist_btn.setOnAction(e -> showTransactionHistoryScreen());
        Open_acc_btn.setOnAction(e -> handleOpenAccount());
    }

    private void updateBalance() {
        if (customer != null) {
            double totalBalance = customer.getAccounts().stream()
                    .mapToDouble(account -> account.getBalance())
                    .sum();
            Available_balance.setText(String.format("P%.2f", totalBalance));
        }
    }

    private void handleOpenAccount() {
        String accountType = accountTypeChoiceBox.getValue();

        if (accountType == null) {
            showAlert("Error", "Please select an account type");
            return;
        }

        try {
            switch (accountType) {
                case "Savings Account":
                    SavingsAccount savingsAccount = new SavingsAccount(
                            generateAccountNumber(), 0.0, customer, "Main Branch");
                    customer.addAccount(savingsAccount);
                    break;

                case "Investment Account":
                    InvestmentAccount investmentAccount = new InvestmentAccount(
                            generateAccountNumber(), 500.0, customer, "Main Branch");
                    customer.addAccount(investmentAccount);
                    break;

                case "Cheque Account":
                    ChequeAccount chequeAccount = new ChequeAccount(
                            generateAccountNumber(), 0.0, customer, "Main Branch",
                            "N/A", "N/A"); // You might want to collect company info
                    customer.addAccount(chequeAccount);
                    break;
            }

            showAlert("Success", accountType + " opened successfully!");
            updateBalance();

        } catch (Exception e) {
            showAlert("Error", "Failed to open account: " + e.getMessage());
        }
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }

    private void showDashboard() {
        navigateTo("/com/felix/bankingsystem/view/Dashboard.fxml", "Dashboard");
    }

    private void showDepositScreen() {
        navigateTo("/com/felix/bankingsystem/view/Deposit.fxml", "Deposit Funds");
    }

    private void showWithdrawScreen() {
        navigateTo("/com/felix/bankingsystem/view/Withdraw.fxml", "Withdraw Funds");
    }

    private void showOpenAccountScreen() {
        // Already on open account screen
    }

    private void showPersonalDetailsScreen() {
        navigateTo("/com/felix/bankingsystem/view/Personaldetails.fxml", "Personal Details");
    }

    private void showTransactionHistoryScreen() {
        navigateTo("/com/felix/bankingsystem/view/TransactionHistory.fxml", "Transaction History");
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
        }
        // Add other controllers as needed
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}