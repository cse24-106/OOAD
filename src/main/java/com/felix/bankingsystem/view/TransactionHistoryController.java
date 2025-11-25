package com.felix.bankingsystem.view;

import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.model.Account;
import com.felix.bankingsystem.model.Transaction;
import com.felix.bankingsystem.controller.BankService;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Collectors;


public class TransactionHistoryController {

    @FXML private Label Available_balance;
    @FXML private TableColumn<Transaction, Double> Dash_tranTable_amount;
    @FXML private TableColumn<Transaction, Double> Dash_tranTable_balance_after;
    @FXML private TableColumn<Transaction, String> Dash_tranTable_date;
    @FXML private TableColumn<Transaction, String> Dash_tranTable_tranID;
    @FXML private TableColumn<Transaction, String> Dash_tranTable_type;
    @FXML private TableView<Transaction> Dash_transactions_table;
    @FXML private Button dashboard_btn;
    @FXML private Button deposit_btn;
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
        updateView();
    }

    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    private void setupTableColumns() {
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
                    .collect(Collectors.toList());
            Dash_transactions_table.getItems().setAll(allTransactions);
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
        // Already on transaction history screen
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
            showAlert("Cannot navigate: " + e.getMessage());
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
