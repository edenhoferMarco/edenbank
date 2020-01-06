package de.marcoedenhofer.edenbank.application.bankaccountservice;

import de.marcoedenhofer.edenbank.persistence.entities.*;
import de.marcoedenhofer.edenbank.persistence.repositories.IBankAccountRepository;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BankAccountService implements IBankAccountService {
    private final String COUNTRY_CODE = "DE";
    private final int BANK_CODE = 75030011;
    private final String BIC = "BYEBDEM1RBG";

    // constants for CheckingAccount
    private final double OVERDRAFT_LIMIT_PRIVATE = 1000.00;
    private final double OVERDRAFT_LIMIT_BUSINESS = 4000.00;
    private final float TO_INTEREST_RATE = 0.0f;
    private final float HAVE_INTEREST_RATE = 12.5f;
    private final double TRANSACTION_COST_PRIVATE = 0.0;
    private final double TRANSACTION_COST_BUSINESS = 0.50;

    private final IBankAccountRepository bankAccountRepository;
    private final ICustomerAccountRepository customerAccountRepository;

    public BankAccountService(IBankAccountRepository bankAccountRepository, ICustomerAccountRepository customerAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerAccountRepository = customerAccountRepository;
    }


    @Override
    public void createCheckingAccountForCustomerAccount(CustomerAccount passedAccount) {
        try {
            CustomerAccount customerAccount = loadCustomerAccountWithId(passedAccount.getCustomerAccountId());
            CheckingAccount checkingAccount = createCheckingAccount();
            customerAccount.getBankAccounts().add(checkingAccount);
            customerAccountRepository.save(customerAccount);
        } catch (UsernameNotFoundException usernameEx) {
            // TODO
        }
    }

    @Override
    public void createSavingsAccountForCustomerAccount(CustomerAccount passedAccount) {
        try {
            CustomerAccount customerAccount = loadCustomerAccountWithId(passedAccount.getCustomerAccountId());
            SavingsAccount checkingAccount = createSavingsAccount();
            customerAccount.getBankAccounts().add(checkingAccount);
            customerAccountRepository.save(customerAccount);
        } catch (UsernameNotFoundException usernameEx) {
            // TODO
        }
    }

    @Override
    public void createFixedDepositAccountForCustomerAccount(CustomerAccount passedAccount) {
        try {
            CustomerAccount customerAccount = loadCustomerAccountWithId(passedAccount.getCustomerAccountId());
            FixedDepositAccount checkingAccount = createFixedDepositAccount();
            customerAccount.getBankAccounts().add(checkingAccount);
            customerAccountRepository.save(customerAccount);
        } catch (UsernameNotFoundException usernameEx) {
            // TODO
        }
    }

    private CustomerAccount loadCustomerAccountWithId(long customerAccountId) throws UsernameNotFoundException {
        CustomerAccount customerAccount = customerAccountRepository.findById(customerAccountId)
                .orElseThrow( () -> {
                    throw new UsernameNotFoundException("Kundenaccount mit Nummer: " + customerAccountId + " existiert nicht");
                });

        return customerAccount;
    }

    private CheckingAccount createCheckingAccount() {
        // TODO: CheckingAccount specific values
        CheckingAccount account = new CheckingAccount();
        account = (CheckingAccount) initializeWithConstantValues(account);
        account.setBalance(0);
        account = bankAccountRepository.save(account);
        long bankAccountId = account.getBankAccountId();
        account.setIban(buildIban(bankAccountId));
        account = bankAccountRepository.save(account);

        return account;
    }

    private SavingsAccount createSavingsAccount() {
        // TODO SavingsAccount specific values
        SavingsAccount account = new SavingsAccount();
        account = (SavingsAccount) initializeWithConstantValues(account);
        account.setBalance(0);
        account = bankAccountRepository.save(account);
        long bankAccountId = account.getBankAccountId();
        account.setIban(buildIban(bankAccountId));
        account = bankAccountRepository.save(account);

        return account;
    }

    private FixedDepositAccount createFixedDepositAccount() {
        // TODO FixedDepositAccount specific values
        FixedDepositAccount account = new FixedDepositAccount();
        account = (FixedDepositAccount) initializeWithConstantValues(account);
        account.setBalance(0);
        account = bankAccountRepository.save(account);
        long bankAccountId = account.getBankAccountId();
        account.setIban(buildIban(bankAccountId));
        account = bankAccountRepository.save(account);

        return account;
    }


    private String buildIban(long bankAccountId) {
        return COUNTRY_CODE + BANK_CODE + String.format("%010d", bankAccountId);
    }

    private BankAccount initializeWithConstantValues(BankAccount account) {
        account.setBankCode(BANK_CODE);
        account.setBic(BIC);

        return account;
    }

}
