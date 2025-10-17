package com.felix.bankingsystem.controller;

import com.felix.bankingsystem.model.Customer;
import java.util.*;

public class BankService {
    private Map<String, Customer> customers;

    public BankService() {
        this.customers = new HashMap<>();
    }

    public void addCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
    }

    public Customer findCustomer(String customerId) {
        return customers.get(customerId);
    }

    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
}