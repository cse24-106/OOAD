package com.felix.bankingsystem;

import java.util.ArrayList;
import java.util.List;

public abstract class Customer {
    protected String customerID;
    protected String address;
    protected List<Account> accounts;

    public Customer(String customerID, String address) {
        this.customerID = customerID;
        this.address = address;
        this.accounts = new ArrayList<>();
    }

    public void openAccount(Account account) {
        accounts.add(account);
    }

    public void closeAccount(Account account) {
        accounts.remove(account);
    }

    public void depositToAccount(String accountNumber, double amount) {
        for (Account acc : accounts){
            if (acc.getAccountNumber().equals(accountNumber)){
                acc.deposit(amount);
            }
        }
    }

    public void withdrawFromAccount(String accountNumber, double amount) {
        for (Account acc : accounts){
            if (acc.getAccountNumber().equals(accountNumber)) {
                acc.withdraw(amount);
            }
        }
    }

    public List<Account> getAccounts(){
        return accounts;
    }
}
