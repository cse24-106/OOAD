package com.felix.bankingsystem.controller;

import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.model.IndividualCustomer;
import java.util.Map;

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
        Map<String, Customer> customers = bankService.getCustomers();

        // Try to find individual by email
        for (Customer c : customers.values()) {
            if (c instanceof IndividualCustomer ind) {
                if (ind.getEmail() != null && ind.getEmail().equalsIgnoreCase(identifier)
                        && ind.getPassword().equals(password)) {
                    return ind;
                }
            }
        }

        // Try by CustomerID
        Customer org = customers.get(identifier);
        if (org != null && org.getPassword().equals(password)) {
            return org;
        }

        return null;
    }
}
