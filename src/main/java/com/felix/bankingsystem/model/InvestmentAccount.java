package com.felix.bankingsystem.model;

import javafx.beans.value.ObservableValue;

public class InvestmentAccount extends Account implements InterestPayable, Withdraw {
    private static final double MIN_INITIAL_DEPOSIT = 500.00;

    public InvestmentAccount(String accountNumber, Customer customer, double initialDeposit) {
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
        double interestRate = 0.05;
        return balance * interestRate;
    }

    @Override
    public void applyInterest(){
        double interest = calculateInterest();
        deposit(interest);
    }

    @Override
    public void Withdraw(double amount) {
        if (amount > 0 && balance >= amount) {
            balance -= amount;
        }
    }

    @Override
    public void deposit(double amount) {
        if (amount < 500) {
            throw new IllegalArgumentException("Minimum deposit for Savings Account is P500.00");
        }
        super.deposit(amount);
    }

    @Override
    public ObservableValue<String> accountNumberProperty() {
        return null;
    }
}