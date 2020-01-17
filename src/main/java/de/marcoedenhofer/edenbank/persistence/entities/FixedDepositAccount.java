package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class FixedDepositAccount extends BankAccount {
    private float interestRate;
    private Date endDate;
    private boolean isDone = false;

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
