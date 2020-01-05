package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String country;
    private String city;
    private Integer postalNumber;
    private String streetName;
    private String houseNumber;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPostalNumber() {
        return postalNumber;
    }

    public void setPostalNumber(int postalNumber) {
        this.postalNumber = postalNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}
