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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.io.IOException;
import java.util.List;

public class DashboardController {

    @FXML
    private Label Available_balance;
    @FXML
    private TableColumn<Account, String> Dash_accTable_accNum;
    @FXML
    private TableColumn<Account, Double> Dash_accTable_available_balance;
    @FXML
    private TableView<Account> Dash_accounts_table;
    @FXML
    private TableColumn<Transaction, Double> Dash_tranTable_amount;
    @FXML
    private TableColumn<Transaction, Double> Dash_tranTable_balance_after;
    @FXML
    private TableColumn<Transaction, String> Dash_tranTable_date;
    @FXML
    private TableColumn<Transaction, String> Dash_tranTable_type;
    @FXML
    private TableColumn<Transaction, String> Dash_tranTable_tranID;
    @FXML
    private TableView<Transaction> Dash_transactions_table;
    @FXML
    private Button dashboard_btn;
    @FXML
    private Button deposit_btn;
    @FXML private Button logout_btn;
    @FXML private Button open_acc_btn;
    @FXML private Button pers_det_btn;
    @FXML private Button transaction_hist_btn;
    @FXML private Button withdraw_btn;

    private Customer customer;
    private BankService bankService;

    @FXML
    private void initialize() {
        setupTableColumns();
        setupButtonActions();
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        updateDashboard();
    }

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    private void setupTableColumns() {
        // Accounts table
        Dash_accTable_accNum.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAccountNumber()));
        Dash_accTable_available_balance.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getBalance()).asObject());

        // Transactions table
        Dash_tranTable_tranID.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTransactionID()));
        Dash_tranTable_date.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDate()));
        Dash_tranTable_type.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getType()));
        Dash_tranTable_amount.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getAmount()).asObject());
        Dash_tranTable_balance_after.setCellValueFactory(cellData ->
                new SimpleDoubleProperty(cellData.getValue().getBalanceAfter()).asObject());
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
    }

    private void updateDashboard() {
        if (customer == null) return;

        // Update account table
        List<Account> accounts = customer.getAccounts();
        Dash_accounts_table.getItems().setAll(accounts);

        // Calculate total available balance
        double totalBalance = accounts.stream()
                .mapToDouble(Account::getBalance)
                .sum();
        Available_balance.setText(String.format("P%.2f", totalBalance));

        // Update transaction table
        List<Transaction> transactions = accounts.stream()
                .flatMap(acc -> acc.getTransactions().stream())
                .toList();
        Dash_transactions_table.getItems().setAll(transactions);
    }

    @FXML
    private void showDashboard() {
        // Already on dashboard
    }

    @FXML
    private void showDepositScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/Deposit.fxml"));
            Parent root = loader.load();

            DepositController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setBankService(bankService);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Deposit Funds");

        } catch (IOException e) {
            showAlert("Cannot load deposit screen: " + e.getMessage());
        }
    }

    @FXML
    private void showWithdrawScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/Withdraw.fxml"));
            Parent root = loader.load();

            WithdrawController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setBankService(bankService);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Withdraw Funds");

        } catch (IOException e) {
            showAlert("Cannot load withdraw screen: " + e.getMessage());
        }
    }

    @FXML
    private void showOpenAccountScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/OpenAccount.fxml"));
            Parent root = loader.load();

            OpenAccountController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setBankService(bankService);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Open New Account");

        } catch (IOException e) {
            showAlert("Cannot load open account screen: " + e.getMessage());
        }
    }

    @FXML
    private void showPersonalDetailsScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/PersonalDetails.fxml"));
            Parent root = loader.load();

            PersonalDetailsController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setBankService(bankService);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Personal Details");

        } catch (IOException e) {
            showAlert("Cannot load personal details screen: " + e.getMessage());
        }
    }

    @FXML
    private void showTransactionHistoryScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/FXML files/TransationHistory.fxml"));
            Parent root = loader.load();

            TransactionHistoryController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setBankService(bankService);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Transaction History");

        } catch (IOException e) {
            showAlert("Cannot load transaction history screen: " + e.getMessage());
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
            showAlert("Cannot logout: " + e.getMessage());
        }
    }

    @FXML
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}