package de.marcoedenhofer.edenbank.application.registrationservice;

import de.marcoedenhofer.edenbank.persistence.entities.BusinessCustomer;
import de.marcoedenhofer.edenbank.persistence.entities.CustomerAccount;
import de.marcoedenhofer.edenbank.persistence.entities.PrivateCustomer;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IRegistrationService extends UserDetailsService {
    CustomerAccount createPrivateCustomerAccount(PrivateCustomer customer);
    void createBusinessCustomerAccount(BusinessCustomer customer);
    CustomerAccount loadCustomerAccountWithId(long customerAccountId);
}
