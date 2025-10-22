package com.felix.bankingsystem.model;

public class IndividualCustomer extends Customer {
    private String firstName;
    private String surname;
    private String nationalID;
    private String address;
    private String phoneNumber;
    private String email;
    private String source_of_funds;

    public IndividualCustomer(String customerID, String address, String firstName, String surname, String nationalID, String phoneNumber, String email) {
        super(customerID, address);
        this.firstName = firstName;
        this.surname = surname;
        this.nationalID = nationalID;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.source_of_funds = source_of_funds;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public String getNationalID() {
        return nationalID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getSourceOfFunds() {
        return source_of_funds;
    }

    @Override
    public String getDisplayName() {
        return firstName + " " + surname;
    }

    public String getFullName() {
        return firstName + " " + surname;
    }
}
