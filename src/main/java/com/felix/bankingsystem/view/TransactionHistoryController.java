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


public class TransactionHistoryController {

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
    private TableView<?> Dash_transactions_table;

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
        updateView();
    }

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    private void setupTableColumns() {
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

    private void updateView() {
        if (customer != null) {
            // Update balance
            double totalBalance = customer.getAccounts().stream()
                    .mapToDouble(Account::getBalance)
                    .sum();
            Available_balance.setText(String.format("P%.2f", totalBalance));

            // Update transactions table
            updateTransactionTable();
        }
    }

    private void updateTransactionTable() {
        if (customer != null) {
            var allTransactions = customer.getAccounts().stream()
                    .flatMap(account -> account.getTransactions().stream())
                    .sorted(Comparator.comparing(Transaction::getDate).reversed())
                    .toList();
            Dash_transactions_table.getItems().setAll(allTransactions);
        }
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
        navigateTo("/com/felix/bankingsystem/view/Openaccount.fxml", "Open Account");
    }

    private void showPersonalDetailsScreen() {
        navigateTo("/com/felix/bankingsystem/view/Personaldetails.fxml", "Personal Details");
    }

    private void showTransactionHistoryScreen() {
        // Already on transaction history screen
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
