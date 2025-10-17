package com.felix.bankingsystem.model;

public class IndividualSavingInterest implements InterestCalculation{
    @Override
    public double calculateInterest(double balance){
        return balance * 0.025;
    }
}
