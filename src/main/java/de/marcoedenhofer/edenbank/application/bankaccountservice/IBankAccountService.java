package de.marcoedenhofer.edenbank.application.bankaccountservice;

import de.marcoedenhofer.edenbank.persistence.entities.*;

public interface IBankAccountService {
    void createCheckingAccountForCustomerAccount(CustomerAccount customerAccount);
    void createSavingsAccountForCustomerAccount(CustomerAccount customerAccount);
    void createFixedDepositAccountForCustomerAccount(CustomerAccount customerAccount);
    void createCheckingAccountWithFixedBudged(CustomerAccount customerAccount, int budget);
    BankAccount loadBankAccountWithId(long bankAccountId);
    BankAccount loadBankAccountWithIban(String iban);
    void archiveBankAccount(BankAccount bankAccount);
}
