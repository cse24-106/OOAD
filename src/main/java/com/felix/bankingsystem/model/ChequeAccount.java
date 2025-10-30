package com.felix.bankingsystem.model;

import javafx.beans.value.ObservableValue;

public class ChequeAccount extends Account implements Withdraw {
    private String employer;
    private String employerAddress;

    public ChequeAccount(String accountNumber, double v, Customer customer, String employer, String employerAddress) {
        super(accountNumber, customer);
        this.employer = employer;
        this.employerAddress = employerAddress;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getEmployerAddress() {
        return employerAddress;
    }

    public void setEmployerAddress(String employerAddress) {
        this.employerAddress = employerAddress;
    }

    @Override
    public void Withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
        }
    }

    @Override
    public double deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
        return amount;
    }

    @Override
    public ObservableValue<String> accountNumberProperty() {
        return null;
    }
}
