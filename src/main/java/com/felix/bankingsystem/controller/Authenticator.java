package com.felix.bankingsystem.controller;

import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.model.IndividualCustomer;

public class Authenticator {
    private final String identifier;
    private final String password;
    private final BankService bankService;

    public Authenticator(String identifier, String password, BankService bankService) {
        this.identifier = identifier;
        this.password = password;
        this.bankService = bankService;
    }

    public Customer login() {
        if (bankService == null) {
            throw new IllegalStateException("BankService is not initialized");
        }

        // Check individuals by email
        for (Customer c : bankService.getAllCustomers()) {
            if (c instanceof IndividualCustomer ind) {
                if (ind.getEmail().equalsIgnoreCase(identifier)
                        && ind.getPassword().equals(password)) {
                    return ind; //Individual login successful
                }
            }
        }

        // Check organizations by CustomerID
        Customer org = bankService.getCustomerById(identifier);
        if (org != null && org.getPassword().equals(password)) {
            return org; //Organization login successful
        }
        return null;
    }
}
