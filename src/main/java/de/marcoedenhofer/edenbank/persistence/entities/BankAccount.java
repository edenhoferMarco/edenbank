package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class BankAccount extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bankAccountId;
    private int bankCode;
    private String iban;
    private String bic;
    private long balance;
    private boolean isArchived = false;
    private int overdraftLimit = 0;

    @Override
    public Long getId() {
        return bankAccountId;
    }

    public long getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(long bankAccountId) {
        this.bankAccountId = bankAccountId;
        super.setId(bankAccountId);
    }

    public int getBankCode() {
        return bankCode;
    }

    public void setBankCode(int bankCode) {
        this.bankCode = bankCode;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public int getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(int overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }
}
