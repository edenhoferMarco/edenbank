package de.marcoedenhofer.edenbank.application.registrationservice;

import de.marcoedenhofer.edenbank.persistence.entities.BusinessCustomer;
import de.marcoedenhofer.edenbank.persistence.entities.Customer;
import de.marcoedenhofer.edenbank.persistence.entities.CustomerAccount;
import de.marcoedenhofer.edenbank.persistence.entities.PrivateCustomer;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerAccountRepository;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Qualifier("security")
public class RegistrationService implements IRegistrationService {
    private final double PRIVATE_MANAGEMENT_FEE = 20;
    private final double BUSINESS_MANAGEMENT_FEE = 150;

    private final ICustomerRepository customerRepository;
    private final ICustomerAccountRepository customerAccountRepository;
    private final BCryptPasswordEncoder encoder;

    @Autowired
    public RegistrationService(ICustomerRepository customerRepository,
                               ICustomerAccountRepository customerAccountRepository,
                               BCryptPasswordEncoder encoder) {
        this.customerRepository = customerRepository;
        this.customerAccountRepository = customerAccountRepository;
        this.encoder = encoder;
    }

    @Override
    public CustomerAccount createPrivateCustomerAccount(PrivateCustomer customer) {
        customer = this.customerRepository.save(customer);

        // TODO: call elyes for postident

        CustomerAccount account = createCustomerAccount(PRIVATE_MANAGEMENT_FEE, customer);
        return customerAccountRepository.save(account);
    }

    @Override
    public void createBusinessCustomerAccount(BusinessCustomer customer) {
        this.customerRepository.save(customer);

        CustomerAccount account = createCustomerAccount(BUSINESS_MANAGEMENT_FEE, customer);
        customerAccountRepository.save(account);
    }

    @Override
    public CustomerAccount loadCustomerAccountWithId(long customerAccountId) throws UsernameNotFoundException {
        CustomerAccount customerAccount = customerAccountRepository.findById(customerAccountId)
                .orElseThrow( () -> {
                    throw new UsernameNotFoundException("Kundenaccount mit Nummer: " + customerAccountId + " existiert nicht");
                });

        return customerAccount;
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

    @Override
    public UserDetails loadUserByUsername(String customerAccountId) throws UsernameNotFoundException {
        CustomerAccount account = customerAccountRepository.findById(Long.parseLong(customerAccountId))
                .orElseThrow( () -> {
                    throw new UsernameNotFoundException("Kundenaccount mit Nummer: " + customerAccountId + " existiert nicht");
                });

        return account;
    }


}