package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
public class CheckingAccount extends BankAccount {
    private float toInterestRate;
    private float haveInterestRate;
    private double transactionCost;

    public float getToInterestRate() {
        return toInterestRate;
    }

    public void setToInterestRate(float toInterestRate) {
        this.toInterestRate = toInterestRate;
    }

    public float getHaveInterestRate() {
        return haveInterestRate;
    }

    public void setHaveInterestRate(float haveInterestRate) {
        this.haveInterestRate = haveInterestRate;
    }

    public double getTransactionCost() {
        return transactionCost;
    }

    public void setTransactionCost(double transactionCost) {
        this.transactionCost = transactionCost;
    }

}
