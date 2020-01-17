package de.marcoedenhofer.edenbank.persistence.entities;

import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Customer extends AbstractEntity<String> {
    @Id
    @NonNull
    private String email;
    private PersonalData personalData;
    private boolean isIdentified = false;

    public Customer() {
    }

    @Override
    public String getId() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        super.setId(email);
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }

    public boolean isIdentified() {
        return isIdentified;
    }

    public void setIdentified(boolean identified) {
        isIdentified = identified;
    }
}
