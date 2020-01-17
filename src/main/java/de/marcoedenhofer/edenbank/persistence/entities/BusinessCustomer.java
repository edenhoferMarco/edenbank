package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Entity;

@Entity
public class BusinessCustomer extends Customer {
    private String companyName;
    private Address companyAddress;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Address getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(Address companyAddress) {
        this.companyAddress = companyAddress;
    }
}
