package com.felix.bankingsystem.model;

public class CompanyCustomer extends Customer {
    private String companyName;
    private String registrationNumber;
    private String contactPerson;
    private String contactPhone;

    public CompanyCustomer(String customerId, String companyName, String registrationNumber,
                           String address, String contactPerson, String contactPhone) {
        super(customerId, address);
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    @Override
    public String getDisplayName() {
        return companyName;
    }
}
