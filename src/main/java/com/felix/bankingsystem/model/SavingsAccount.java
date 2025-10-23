package com.felix.bankingsystem.model;

import javafx.beans.value.ObservableValue;

public class SavingsAccount extends Account implements InterestPayable {
    private double interestRate = 0.05;
    private static final double MIN_INITIAL_DEPOSIT = 50.00;

    public SavingsAccount(String accountNumber, Customer customer, double initialDeposit) {
        super(accountNumber, customer);

        if (initialDeposit < MIN_INITIAL_DEPOSIT) {
            throw new IllegalArgumentException(
                    "Initial deposit for Investment Account must be at least P500.00."
            );
        } else {
            this.balance = initialDeposit;
        }
    }

    @Override
    public double calculateInterest() {
        return balance * interestRate;
    }

    @Override
    public void applyInterest() {
        double interest = calculateInterest();
        deposit(interest);
    }

    @Override
    public ObservableValue<String> accountNumberProperty() {
        return null;
    }
}
