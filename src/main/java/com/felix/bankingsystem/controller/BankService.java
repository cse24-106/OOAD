package com.felix.bankingsystem.controller;

import com.felix.bankingsystem.model.*;
import javafx.scene.control.Alert;
import java.util.*;

public class BankService {
    private Map<String, Customer> customers;
    private DatabaseHandler database;

    public BankService() {
        this.database = new DatabaseHandler();
        this.customers = database.loadCustomers();
        database.loadAccounts(customers);
        database.loadTransactions(customers);

        System.out.println("BankService initialized. Customers loaded: " + customers.size());
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
        saveAllData();
        System.out.println("Customer added: " + customer.getDisplayName());
    }

    public Customer findCustomer(String customerId) {
        return customers.get(customerId);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }


    public void openInvestmentAccount(Customer customer, String accountNumber, double initialDeposit) {
        try {
            InvestmentAccount account = new InvestmentAccount(accountNumber, customer, initialDeposit);
            customer.openAccount(account);
            saveAllData();

            // Show success alert
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Investment account created successfully!");
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Deposit", e.getMessage());
        }
    }

    public void openSavingsAccount(Customer customer, String accountNumber, double initialDeposit) {
        try {
            SavingsAccount account = new SavingsAccount(accountNumber, customer, initialDeposit);
            customer.openAccount(account);
            saveAllData();

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Savings account created successfully!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    public void openChequeAccount(Customer customer, String accountNumber, String employer, String employerAddress) {
        try {
            ChequeAccount account = new ChequeAccount(accountNumber, 0.0, customer, employer, employerAddress);
            customer.openAccount(account);
            saveAllData();

            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Cheque account created successfully!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }


    public void deposit(Customer customer, double amount) {
        if (customer.getAccounts().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Deposit Failed", "No account found for deposit.");
            return;
        }

        Account account = customer.getAccounts().get(0);
        account.deposit(amount);

        Transaction transaction = new Transaction(
                "T" + System.currentTimeMillis(),
                new Date().toString(),
                "DEPOSIT",
                amount,
                account.getBalance()
        );

        account.addTransaction(transaction);
        saveAllData();

        showAlert(Alert.AlertType.INFORMATION, "Deposit Successful",
                "Deposited P" + amount + " into account " + account.getAccountNumber());
    }


    public void withdraw(Customer customer, double amount) {
        if (customer.getAccounts().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Withdraw Failed", "No account found for withdrawal.");
            return;
        }

        Account account = customer.getAccounts().get(0);

        if (amount <= 0) {
            showAlert(Alert.AlertType.ERROR, "Withdraw Failed", "Amount must be greater than 0.");
            return;
        }

        if (account.getBalance() < amount) {
            showAlert(Alert.AlertType.ERROR, "Withdraw Failed", "Insufficient funds.");
            return;
        }

        account.withdraw(amount);

        Transaction transaction = new Transaction(
                "T" + System.currentTimeMillis(),
                new Date().toString(),
                "WITHDRAWAL",
                amount,
                account.getBalance()
        );

        account.addTransaction(transaction);
        saveAllData();

        showAlert(Alert.AlertType.INFORMATION, "Withdrawal Successful",
                "Withdrew P" + amount + " from account " + account.getAccountNumber());
    }


    private void saveAllData() {
        database.saveCustomers(customers.values());
        database.saveAccounts(customers.values());
        database.saveTransactions(customers.values());
        System.out.println("Data saved successfully.");
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}