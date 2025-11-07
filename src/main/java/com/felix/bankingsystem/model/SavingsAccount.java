package com.felix.bankingsystem.model;

import javafx.beans.value.ObservableValue;

public class SavingsAccount extends Account implements InterestPayable {
    private static final double MIN_INITIAL_DEPOSIT = 50.00;

    public SavingsAccount(String accountNumber, Customer customer, double initialDeposit) {
        super(accountNumber, customer);

        if (initialDeposit < MIN_INITIAL_DEPOSIT) {
            throw new IllegalArgumentException(
                    "Initial deposit for Savings Account must be at least P50.00."
            );
        } else {
            this.balance = initialDeposit;
        }
    }

    @Override
    public void deposit(double amount) {
        if (amount < 50) {
            throw new IllegalArgumentException("Minimum deposit for Savings Account is P50.00");
        }
        super.deposit(amount);
    }

    @Override
    public double calculateInterest() {
        double interestRate = 0.05;
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
