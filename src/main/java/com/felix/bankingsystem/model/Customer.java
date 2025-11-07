package com.felix.bankingsystem.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Customer {
    protected String customerID;
    protected String address;
    protected List<Account> accounts;
    protected String password;

    public Customer(String customerID, String address) {
        this.customerID = customerID;
        this.address = address;
        this.accounts = new ArrayList<>();
    }

    public String getCustomerId() { return customerID; }
    public String getAddress() { return address; }
    public List<Account> getAccounts() { return accounts; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addAccount(Account account) {
        accounts.add(account);
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

    public abstract String getDisplayName();

    public void setNationalID(String nationalId) {
    }
}
