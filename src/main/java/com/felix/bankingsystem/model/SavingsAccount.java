package com.felix.bankingsystem.model;

import javafx.beans.value.ObservableValue;

public class SavingsAccount extends Account implements InterestCalculation {
    private InterestCalculation interestRate;

    public SavingsAccount(String accountNumber, double customer, Customer interestRate) {
        super(accountNumber, customer);
        this.interestRate = (InterestCalculation) interestRate;
    }

    @Override
    public double calculateInterest(double balance) {
        return interestRate.calculateInterest(balance);
    }

    public void applyMonthlyInterest() {
        double interest = calculateInterest(balance);
        deposit(interest);
    }

    @Override
    public ObservableValue<String> accountNumberProperty() {
        return null;
    }
}
