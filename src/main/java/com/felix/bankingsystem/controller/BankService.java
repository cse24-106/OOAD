package com.felix.bankingsystem.controller;

import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.model.InvestmentAccount;
import javafx.scene.control.Alert;

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
}