package com.felix.bankingsystem.model;

import javafx.beans.value.ObservableValue;
import java.util.ArrayList;
import java.util.List;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected Customer customer;

    public Account(String accountNumber, Customer customer) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = 0.0;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public double deposit(double amount) {
        return amount;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    private List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public abstract ObservableValue<String> accountNumberProperty();


    public double withdraw(double amount) {
        return amount;
    }
}
