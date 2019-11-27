package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Embeddable
public class PersonalData {
    private String firstname;
    private String lastname;
    @Temporal(TemporalType.DATE)
    private Date birthdate;
    private String birthPlace;
    private String gender;
    private Address personalAddress;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Address getPersonalAddress() {
        return personalAddress;
    }

    public void setPersonalAddress(Address personalAddress) {
        this.personalAddress = personalAddress;
    }
}
