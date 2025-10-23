package com.felix.bankingsystem.controller;

import com.felix.bankingsystem.model.Customer;
import com.felix.bankingsystem.model.IndividualCustomer;

public class Authenticator {
    private final String username;
    private final String password;

    public Authenticator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Customer login() {
        // TODO: Add actual authentication logic, for now just a dummy example:
        if (username.equals("admin") && password.equals("1234")) {
            return new IndividualCustomer("001", "Admin", "Admin St", "hjv", "9897", "45678", "dyuf");
        } else {
            return null;
        }
    }
}
