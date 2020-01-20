package de.marcoedenhofer.edenbank.application.customeraccountservice;

import de.marcoedenhofer.edenbank.application.transactionservice.TransactionData;
import de.marcoedenhofer.edenbank.persistence.entities.BusinessCustomer;
import de.marcoedenhofer.edenbank.persistence.entities.CustomerAccount;
import de.marcoedenhofer.edenbank.persistence.entities.PrivateCustomer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface ICustomerAccountService extends UserDetailsService {
    CustomerAccount createPrivateCustomerAccount(PrivateCustomer customer) throws PostIdentException;
    CustomerAccount createBusinessCustomerAccount(BusinessCustomer customer);
    TransactionData callGiveawayService(CustomerAccount customerAccount) throws GiveawayException;
    CustomerAccount loadCustomerAccountWithId(long customerAccountId) throws UsernameNotFoundException;
    CustomerAccount loadCustomerAccountWithId(String customerAccountId) throws UsernameNotFoundException;
    boolean customerAccountOwnsBankAccountWithId(CustomerAccount customerAccount, long bankAccountId);
}
