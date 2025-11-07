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

    public Map<String, Customer> getCustomers() {
        return customers;
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
        saveAllData();
        System.out.println("Customer added: " + customer.getDisplayName());
    }

    public void depositIntoAccount(Customer customer, Account account, double amount) {

        if (customer.getAccounts().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Deposit Failed", "No account found for deposit.");
            return;
        }

        if (amount <= 0) {
            showAlert(Alert.AlertType.ERROR, "Deposit Failed", "Deposit amount must be greater than zero.");
            return;
        }

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


    public void withdrawFromAccount(Customer customer, Account account, double amount) {
        account.withdraw(amount);

        if (customer.getAccounts().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Withdrawal Failed", "No account found for withdrawal.");
            return;
        }

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

    public Customer getCustomerById(String id) {
        return customers.get(id);
    }

    public void saveAllData() {
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