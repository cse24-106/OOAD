package com.felix.bankingsystem;

public class InvestmentAccount extends Account implements InterestPayable{
    private double interestRate = 0.05;

    public InvestmentAccount(String accountNumber, Customer customer) {
        super(accountNumber, customer);
    }

    @Override
    public double calculateInterest() {
        return balance * interestRate;
    }

    @Override
    public void applyInterest(){
        double interest = calculateInterest();
        deposit(interest);
    }
}
