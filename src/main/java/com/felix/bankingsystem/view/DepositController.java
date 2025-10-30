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
import java.util.Comparator;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class DepositController {

    @FXML
    private Label Available_balance;

    @FXML
    private TableColumn<Transaction, Double> Dash_tranTable_amount;

    @FXML
    private TableColumn<Transaction, Double> Dash_tranTable_balance_after;

    @FXML
    private TableColumn<Transaction, String> Dash_tranTable_date;

    @FXML
    private TableColumn<Transaction, String> Dash_tranTable_tranID;

    @FXML
    private TableColumn<Transaction, String> Dash_tranTable_type;

    @FXML
    private TableView<Transaction> Dash_transactions_table;

    @FXML
    private TextField Dep_amount;

    @FXML
    private Button Deposit_btn;

    @FXML
    private Button dashboard_btn;

    @FXML
    private Button deposit_btn;

    @FXML
    private Button open_acc_btn;

    @FXML
    private Button pers_det_btn;

    @FXML
    private Button transaction_hist_btn;

    @FXML
    private Button withdraw_btn;

    @FXML
    private Button logout_btn;

    private Customer customer;
    private BankService bankService;
    private Account selectedAccount;

    @FXML
    private void initialize() {
        setupTableColumns();
        setupButtonActions();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        updateView();
    }

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    private void setupTableColumns() {
        Dash_tranTable_tranID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTransactionID()));
        Dash_tranTable_date.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        Dash_tranTable_type.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));
        Dash_tranTable_amount.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
        Dash_tranTable_balance_after.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getBalanceAfter()).asObject());
    }

    @FXML
    private void setupButtonActions() {
        dashboard_btn.setOnAction(e -> showDashboard());
        deposit_btn.setOnAction(e -> showDepositScreen());
        withdraw_btn.setOnAction(e -> showWithdrawScreen());
        open_acc_btn.setOnAction(e -> showOpenAccountScreen());
        pers_det_btn.setOnAction(e -> showPersonalDetailsScreen());
        transaction_hist_btn.setOnAction(e -> showTransactionHistoryScreen());
        Deposit_btn.setOnAction(e -> handleDeposit());
    }

    private void updateView() {
        if (customer != null) {
            // Calculate total balance
            double totalBalance = customer.getAccounts().stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            Available_balance.setText(String.format("P%.2f", totalBalance));

            // Show deposit transactions
            updateTransactionTable();
        }
    }

    private void updateTransactionTable() {
        if (customer != null) {
            var depositTransactions = customer.getAccounts().stream()
                    .flatMap(account -> account.getTransactions().stream())
                    .filter(transaction -> "DEPOSIT".equals(transaction.getType()))
                    .sorted(Comparator.comparing(Transaction::getDate).reversed())
                    .toList();
            Dash_transactions_table.getItems().setAll(depositTransactions);
        }
    }

    @FXML
    private void handleDeposit() {
        try {
            double amount = Double.parseDouble(Dep_amount.getText().trim());

            if (amount <= 0) {
                showAlert("Error", "Amount must be greater than 0");
                return;
            }

            if (customer == null || customer.getAccounts().isEmpty()) {
                showAlert("Error", "No accounts available for deposit");

                // Create transaction
                Transaction transaction = new Transaction(
                        "T" + System.currentTimeMillis(),
                        "DEPOSIT",
                        amount,
                        selectedAccount.getBalance() + amount
                );

                // Update account balance
                bankService.deposit(customer, amount);
                updateView();

                showAlert("Success", String.format("Deposited P%.2f successfully", amount));
                Dep_amount.clear();
                updateView();

            } else {
                showAlert("Error", "No accounts available for deposit");
            }

        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        }
    }

    @FXML
    private void showDashboard() {
        navigateTo("/com/felix/bankingsystem/view/Dashboard.fxml", "Dashboard");
    }

    @FXML
    private void showDepositScreen() {
        // Already on deposit screen
    }

    @FXML
    private void showWithdrawScreen() {
        navigateTo("/com/felix/bankingsystem/view/Withdraw.fxml", "Withdraw Funds");
    }

    @FXML
    private void showOpenAccountScreen() {
        navigateTo("/com/felix/bankingsystem/view/Openaccount.fxml", "Open Account");
    }

    @FXML
    private void showPersonalDetailsScreen() {
        navigateTo("/com/felix/bankingsystem/view/Personaldetails.fxml", "Personal Details");
    }

    @FXML
    private void showTransactionHistoryScreen() {
        navigateTo("/com/felix/bankingsystem/view/TransactionHistory.fxml", "Transaction History");
    }

    @FXML
    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            // Set customer and bank service for the new controller
            Object controller = loader.getController();
            if (controller instanceof DashboardController) {
                ((DashboardController) controller).setCustomer(customer);
                ((DashboardController) controller).setBankService(bankService);
            } else if (controller instanceof DepositController) {
                ((DepositController) controller).setCustomer(customer);
                ((DepositController) controller).setBankService(bankService);
            }
            // Add other controller types as needed...

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);

        } catch (IOException e) {
            showAlert("Error", "Cannot navigate: " + e.getMessage());
        }
    }

    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/view/Login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) logout_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Capital Bank - Login");
            stage.centerOnScreen();

        } catch (IOException e) {
            showAlert("Error", "Cannot logout: " + e.getMessage());
        }
    }

    @FXML
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
