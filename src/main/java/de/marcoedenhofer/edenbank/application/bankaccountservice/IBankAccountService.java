package de.marcoedenhofer.edenbank.application.bankaccountservice;

import de.marcoedenhofer.edenbank.persistence.entities.*;

import java.util.List;

public interface IBankAccountService {
    void createCheckingAccountForCustomerAccount(CustomerAccount customerAccount);
    void createSavingsAccountForCustomerAccount(CustomerAccount customerAccount);
    void createFixedDepositAccountForCustomerAccount(CustomerAccount customerAccount);
    void createCheckingAccountWithFixedBudged(CustomerAccount customerAccount, int budget);
    BankAccount loadBankAccountWithId(long bankAccountId) throws BankAccountNotFoundException;
    BankAccount loadBankAccountWithIban(String iban) throws BankAccountNotFoundException;
    void archiveBankAccount(BankAccount bankAccount);
    List<BankAccount> loadAllActiveBankAccountsFromCustomerAccount(CustomerAccount customerAccount);
    List<BankAccount> loadAllActiveBankAccountsFromCustomerAccountExceptId(CustomerAccount customerAccount,
                                                                           long exceptionAccountId);
    List<SavingsAccount> loadAllActiveSavingsAccounts();
    List<FixedDepositAccount> loadAllActiveFixedDepositAccounts();
}
