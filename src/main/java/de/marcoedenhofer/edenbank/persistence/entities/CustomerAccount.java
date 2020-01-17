package de.marcoedenhofer.edenbank.persistence.entities;

import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class CustomerAccount extends AbstractEntity<Long> implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerAccountId;
    private String password;
    private double managementFee;
    private boolean isArchived = false;
    @OneToMany
    private List<BankAccount> bankAccounts = new ArrayList<>();
    @OneToOne
    @Cascade(org.hibernate.annotations.CascadeType.PERSIST)
    private Customer customerDetails;

    @Override
    public Long getId() {
        return customerAccountId;
    }

    public long getCustomerAccountId() {
        return customerAccountId;
    }

    public void setCustomerAccountId(long customerAccountId) {
        this.customerAccountId = customerAccountId;
        super.setId(customerAccountId);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return String.valueOf(customerAccountId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !isArchived;
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

    public List<BankAccount> getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public Customer getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(Customer customerData) {
        this.customerDetails = customerData;
    }
}
