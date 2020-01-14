package de.marcoedenhofer.edenbank.application.customeraccountservice;

import de.marcoedenhofer.edenbank.persistence.entities.*;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerAccountRepository;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Qualifier("security")
public class CustomerAccountService implements ICustomerAccountService {
    private final double PRIVATE_MANAGEMENT_FEE = 20;
    private final double BUSINESS_MANAGEMENT_FEE = 150;

    private final ICustomerRepository customerRepository;
    private final ICustomerAccountRepository customerAccountRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public CustomerAccountService(ICustomerRepository customerRepository,
                                  ICustomerAccountRepository customerAccountRepository,
                                  BCryptPasswordEncoder encoder) {
        this.customerRepository = customerRepository;
        this.customerAccountRepository = customerAccountRepository;
        this.encoder = encoder;
    }

    @Override
    public CustomerAccount createPrivateCustomerAccount(PrivateCustomer customer) {
        customer = customerRepository.save(customer);

        // TODO: call elyes for postident

        CustomerAccount account = createCustomerAccount(PRIVATE_MANAGEMENT_FEE, customer);
        return customerAccountRepository.save(account);
    }

    @Override
    public CustomerAccount createBusinessCustomerAccount(BusinessCustomer customer) {
        customer = customerRepository.save(customer);

        CustomerAccount account = createCustomerAccount(BUSINESS_MANAGEMENT_FEE, customer);
        return customerAccountRepository.save(account);
    }

    @Override
    public CustomerAccount loadCustomerAccountWithId(long customerAccountId) throws UsernameNotFoundException {
        return loadCustomerAccountWithId(String.valueOf(customerAccountId));
    }

    @Override
    public CustomerAccount loadCustomerAccountWithId(String customerAccountId) throws UsernameNotFoundException {
        UserDetails userDetails = loadUserByUsername(customerAccountId);

        if (!(userDetails instanceof CustomerAccount)) {
            throw new UsernameNotFoundException("Account mit Nummer " + customerAccountId + " ist kein Kundenaccount");
        }

        return (CustomerAccount) userDetails;
    }

    @Override
    public UserDetails loadUserByUsername(String customerAccountId) throws UsernameNotFoundException {
        CustomerAccount account = customerAccountRepository.findById(Long.parseLong(customerAccountId))
                .orElseThrow( () -> {
                    throw new UsernameNotFoundException("Kundenaccount mit Nummer: " + customerAccountId + " existiert nicht");
                });

        return account;
    }


    @Override
    public boolean customerAccountOwnsBankAccountWithId(CustomerAccount customerAccount, long bankAccountId) {
        for (BankAccount bankAccount : customerAccount.getBankAccounts()) {
            if (bankAccount.getBankAccountId() == bankAccountId) {
                return true;
            }
        }

        return false;
    }

    private CustomerAccount createCustomerAccount(double managementFee, Customer customer) {
        CustomerAccount account = new CustomerAccount();
        account.setArchived(false);
        // for now use firstname as password
        String password = customer.getPersonalData().getFirstname();
        account.setPassword(encoder.encode(password));
        account.setManagementFee(managementFee);
        account.setCustomerDetails(customer);

        return account;
    }
}