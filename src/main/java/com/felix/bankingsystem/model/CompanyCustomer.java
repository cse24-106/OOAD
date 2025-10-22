package com.felix.bankingsystem.model;

public class CompanyCustomer extends Customer {
    private String companyName;
    private String registrationNumber;
    private String address;
    private String contactPerson;
    private String contactPhone;
    private String tax_ID;
    private String incoporation_Date;

    public CompanyCustomer(String customerId, String companyName, String registrationNumber,
                           String address, String contactPerson, String contactPhone, String tax_ID, String incoporation_Date) {
        super(customerId, address);
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.address = address;
        this.contactPerson = contactPerson;
        this.contactPhone = contactPhone;
        this.tax_ID = tax_ID;
        this.incoporation_Date = incoporation_Date;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getAddress() {
        return address;
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

    public String getTax_ID() {
        return tax_ID;
    }

    public String getIncoporation_Date() {
        return incoporation_Date;
    }
}
