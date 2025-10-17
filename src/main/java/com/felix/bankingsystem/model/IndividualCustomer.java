package com.felix.bankingsystem.model;

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

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
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

    public void setNationalId(String nationalId) { this.nationalID = nationalId; }

    @Override
    public String getDisplayName() {
        return firstName + " " + surname;
    }

    public String getFullName() {
        return firstName + " " + surname;
    }
}
