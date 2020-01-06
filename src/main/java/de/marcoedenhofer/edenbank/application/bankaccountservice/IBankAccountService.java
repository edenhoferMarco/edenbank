package de.marcoedenhofer.edenbank.application.bankaccountservice;

import de.marcoedenhofer.edenbank.persistence.entities.*;

public interface IBankAccountService {
    void createCheckingAccountForCustomerAccount(CustomerAccount customerAccount);
    void createSavingsAccountForCustomerAccount(CustomerAccount customerAccount);
    void createFixedDepositAccountForCustomerAccount(CustomerAccount customerAccount);
}
