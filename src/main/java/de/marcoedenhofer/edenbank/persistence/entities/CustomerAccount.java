package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class CustomerAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerAccountId;
    private String password;
    private double managementFee;
    private boolean isArchived;
    private TanList tanList;
    @OneToMany
    private List<Loan> loans;
    @OneToMany
    private List<BankAccount> bankAccounts;
    private Customer customerData;

    public long getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(long customerAccountId) {
        this.customerAccountId = customerAccountId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(double managementFee) {
        this.managementFee = managementFee;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public TanList getTanList() {
        return tanList;
    }

    public void setTanList(TanList tanList) {
        this.tanList = tanList;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public Customer getCustomerData() {
        return customerData;
    }

    public void setCustomerData(Customer customerData) {
        this.customerData = customerData;
    }
}
