package de.marcoedenhofer.edenbank.application.registrationservice;

import de.marcoedenhofer.edenbank.persistence.entities.BusinessCustomer;
import de.marcoedenhofer.edenbank.persistence.entities.CustomerAccount;
import de.marcoedenhofer.edenbank.persistence.entities.PrivateCustomer;

public interface IRegistrationService {
    CustomerAccount createPrivateCustomerAccount(PrivateCustomer customer);
    void createBusinessCustomerAccount(BusinessCustomer customer);
}
