package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Embeddable
@Inheritance(strategy = InheritanceType.JOINED)
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
