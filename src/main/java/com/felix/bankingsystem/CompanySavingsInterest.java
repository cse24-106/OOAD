package com.felix.bankingsystem;

public class CompanySavingsInterest implements InterestCalculation{
    @Override
    public double calculateInterest(double balance) {
        return balance * 0.075;
    }
}
