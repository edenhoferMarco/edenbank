package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Embeddable;

@Embeddable
public class Customer {
    private PersonalData personalData;

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }
}
