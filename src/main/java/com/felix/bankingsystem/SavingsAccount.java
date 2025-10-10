package com.felix.bankingsystem;

public class SavingsAccount extends Account implements InterestCalculation{
    private InterestCalculation interestRate;

    public SavingsAccount(String accountNumber, Customer customer, InterestCalculation interestRate) {
        super(accountNumber, customer);
        this.interestRate = interestRate;
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
    public void withdraw(double amount) {
        System.out.println("Withdrawal is not allwoed for a Savings Account");
    }
}
