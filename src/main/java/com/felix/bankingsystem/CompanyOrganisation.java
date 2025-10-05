package com.felix.bankingsystem;

public class CompanyOrganisation extends Customer{
    private String organisationName;
    private String registrationNumber;
    private String companyAddress;
    private String contactPerson;
    private String conatactPhone;

    public CompanyOrganisation(String customerID, String address, String organisationName, String registrationNumber, String companyAddress, String contactPerson, String conatactPhone) {
        super(customerID, address);
        this.organisationName = organisationName;
        this.registrationNumber = registrationNumber;
        this.companyAddress = companyAddress;
        this.contactPerson = contactPerson;
        this.conatactPhone = conatactPhone;
    }
}
