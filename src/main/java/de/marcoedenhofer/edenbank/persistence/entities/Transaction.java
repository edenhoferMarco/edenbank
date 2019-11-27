package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long transactionId;
    private double amount;
    private String usageDetails;
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;
    @ManyToOne
    private BankAccount senderBankAccount;
    @ManyToOne
    private BankAccount receiverBankAccount;

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUsageDetails() {
        return usageDetails;
    }

    public void setUsageDetails(String usageDetails) {
        this.usageDetails = usageDetails;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BankAccount getSenderBankAccount() {
        return senderBankAccount;
    }

    public void setSenderBankAccount(BankAccount senderBankAccount) {
        this.senderBankAccount = senderBankAccount;
    }

    public BankAccount getReceiverBankAccount() {
        return receiverBankAccount;
    }

    public void setReceiverBankAccount(BankAccount receiverBankAccount) {
        this.receiverBankAccount = receiverBankAccount;
    }
}
