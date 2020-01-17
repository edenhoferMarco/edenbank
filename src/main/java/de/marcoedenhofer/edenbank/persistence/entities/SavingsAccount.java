package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Entity;

@Entity
public class SavingsAccount extends BankAccount {
    private float interestRate;

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }
}
