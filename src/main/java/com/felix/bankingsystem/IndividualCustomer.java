package com.felix.bankingsystem;

public class IndividualCustomer extends Customer {
    private String firstName;
    private String surname;
    private String nationalID;
    private String phoneNumber;
    private String email;

    public IndividualCustomer(String customerID, String address, String firstName, String surname, String nationalID, String phoneNumber, String email) {
        super(customerID, address);
        this.firstName = firstName;
        this.surname = surname;
        this.nationalID = nationalID;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
}
