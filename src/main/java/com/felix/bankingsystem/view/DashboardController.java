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

    @FXML
    private Button logout_btn;

    @FXML
    private Button open_acc_btn;

    @FXML
    private Button pers_det_btn;

    @FXML
    private Button transaction_hist_btn;

    @FXML
    private Button withdraw_btn;

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
        Dash_accTable_accNum.setCellValueFactory(cellData -> cellData.getValue().accountNumberProperty());
        Dash_accTable_available_balance.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asObject());

        // Transactions table
        Dash_tranTable_tranID.setCellValueFactory(cellData -> cellData.getValue().transactionIdProperty());
        Dash_tranTable_date.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        Dash_tranTable_type.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        Dash_tranTable_amount.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        Dash_tranTable_balance_after.setCellValueFactory(cellData -> cellData.getValue().balanceAfterProperty().asObject());
    }

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
        if (customer != null) {
            // Calculate total balance
            double totalBalance = customer.getAccounts().stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            Available_balance.setText(String.format("P%.2f", totalBalance));

            // Update accounts table
            Dash_accounts_table.getItems().setAll(customer.getAccounts());

            // Update transactions table (get all transactions from all accounts)
            List<Transaction> allTransactions = customer.getAccounts().stream()
                    .flatMap(account -> account.getTransactions().stream())
                    .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
                    .limit(10) // Show only last 10 transactions
                    .toList();
            Dash_transactions_table.getItems().setAll(allTransactions);
        }
    }

    private void showDashboard() {
        // Already on dashboard
    }

    private void showDepositScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/view/Deposit.fxml"));
            Parent root = loader.load();

            DepositController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setBankService(bankService);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Deposit Funds");

        } catch (IOException e) {
            showAlert("Error", "Cannot load deposit screen: " + e.getMessage());
        }
    }

    private void showWithdrawScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/view/Withdraw.fxml"));
            Parent root = loader.load();

            WithdrawController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setBankService(bankService);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Withdraw Funds");

        } catch (IOException e) {
            showAlert("Error", "Cannot load withdraw screen: " + e.getMessage());
        }
    }

    private void showOpenAccountScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/view/Openaccount.fxml"));
            Parent root = loader.load();

            OpenAccountController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setBankService(bankService);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Open New Account");

        } catch (IOException e) {
            showAlert("Error", "Cannot load open account screen: " + e.getMessage());
        }
    }

    private void showPersonalDetailsScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/view/Personaldetails.fxml"));
            Parent root = loader.load();

            PersonalDetailsController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setBankService(bankService);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Personal Details");

        } catch (IOException e) {
            showAlert("Error", "Cannot load personal details screen: " + e.getMessage());
        }
    }

    private void showTransactionHistoryScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/felix/bankingsystem/view/TransactionHistory.fxml"));
            Parent root = loader.load();

            TransactionHistoryController controller = loader.getController();
            controller.setCustomer(customer);
            controller.setBankService(bankService);

            Stage stage = (Stage) dashboard_btn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Transaction History");

        } catch (IOException e) {
            showAlert("Error", "Cannot load transaction history screen: " + e.getMessage());
        }
    }

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}