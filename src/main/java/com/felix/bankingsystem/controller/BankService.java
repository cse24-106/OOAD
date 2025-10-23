package com.felix.bankingsystem.controller;

import com.felix.bankingsystem.model.*;
import javafx.scene.control.Alert;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BankService {
    private Map<String, Customer> customers;

    public BankService() {
        this.customers = new HashMap<>();
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
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

            // Show success alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Investment account created successfully!");
            alert.showAndWait();

        } catch (IllegalArgumentException e) {
            // Show error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Deposit");
            alert.setHeaderText("Account Creation Failed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    public void deposit(Customer customer, double amount) {
        Account account = customer.getAccounts().get(0);
        account.deposit(amount);

        // Generate transaction details
        String transactionId = UUID.randomUUID().toString();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String type = "Deposit";
        double balanceAfter = account.getBalance();

        // Create transaction using your existing constructor
        Transaction transaction = new Transaction(transactionId, date, type, amount, balanceAfter);
        account.addTransaction(transaction);
    }


    public void withdraw(Customer customer, double amount) {
        Account account = customer.getAccounts().get(0);
        account.withdraw(amount);

        String transactionId = UUID.randomUUID().toString();
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String type = "Withdrawal";
        double balanceAfter = account.getBalance();

        Transaction transaction = new Transaction(transactionId, date, type, amount, balanceAfter);
        account.addTransaction(transaction);
    }


    public void applyMonthlyInterestToAllAccounts() {
        for (Customer customer : customers.values()) {
            for (Account account : customer.getAccounts()) {
                if (account instanceof InterestPayable interestAccount) {
                    interestAccount.applyInterest();
                }
            }
        }

    }
}