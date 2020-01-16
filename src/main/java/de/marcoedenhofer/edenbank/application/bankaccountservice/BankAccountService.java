package de.marcoedenhofer.edenbank.application.bankaccountservice;

import de.marcoedenhofer.edenbank.application.customeraccountservice.ICustomerAccountService;
import de.marcoedenhofer.edenbank.persistence.entities.*;
import de.marcoedenhofer.edenbank.persistence.repositories.IBankAccountRepository;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerAccountRepository;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankAccountService implements IBankAccountService {
    private final String COUNTRY_CODE = "DE";
    private final int BANK_CODE = 75030011;
    private final String BIC = "BYEBDEM1RBG";

    private final String EDENBANK_ACCOUNT_IBAN = "DE750300110000000004";

    // constants for CheckingAccount
    private final int OVERDRAFT_LIMIT_PRIVATE = 1000;
    private final int OVERDRAFT_LIMIT_BUSINESS = 4000;
    private final float TO_INTEREST_RATE = 0.0f;
    private final float HAVE_INTEREST_RATE = 12.5f;
    private final int TRANSACTION_COST_PRIVATE = 0;
    private final int TRANSACTION_COST_BUSINESS = 50;

    // constants for SavingsAccount
    private final float SAVINGS_ACCOUNT_INTEREST_RATE = 1f;

    private final IBankAccountRepository bankAccountRepository;
    private final ICustomerAccountRepository customerAccountRepository;
    private final ICustomerAccountService customerAccountService;

    public BankAccountService(IBankAccountRepository bankAccountRepository,
                              ICustomerAccountRepository customerAccountRepository,
                              ICustomerAccountService registrationService) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerAccountRepository = customerAccountRepository;
        this.customerAccountService = registrationService;
    }


    @Override
    public void createCheckingAccountForCustomerAccount(CustomerAccount passedAccount) {
        try {
            CustomerAccount customerAccount = customerAccountService.loadCustomerAccountWithId(
                    passedAccount.getCustomerAccountId());
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
            CustomerAccount customerAccount = customerAccountService.loadCustomerAccountWithId(
                    passedAccount.getCustomerAccountId());
            SavingsAccount account = createSavingsAccount();
            account.setInterestRate(SAVINGS_ACCOUNT_INTEREST_RATE);
            customerAccount.getBankAccounts().add(account);
            customerAccountRepository.save(customerAccount);
        } catch (UsernameNotFoundException usernameEx) {
            // TODO
        }
    }

    @Override
    public void createFixedDepositAccountForCustomerAccount(CustomerAccount passedAccount) {
        try {
            CustomerAccount customerAccount = customerAccountService.loadCustomerAccountWithId(
                    passedAccount.getCustomerAccountId());
            FixedDepositAccount account = createFixedDepositAccount();
            customerAccount.getBankAccounts().add(account);
            customerAccountRepository.save(customerAccount);
        } catch (UsernameNotFoundException usernameEx) {
            // TODO
        }
    }

    @Override
    public void createCheckingAccountWithFixedBudged(CustomerAccount passedAccount, int budget) {
        try {
            CustomerAccount customerAccount = customerAccountService.loadCustomerAccountWithId(
                    passedAccount.getCustomerAccountId());
            CheckingAccount checkingAccount = createCheckingAccount();

            checkingAccount.setBalance(budget);
            bankAccountRepository.save(checkingAccount);

            customerAccount.getBankAccounts().add(checkingAccount);
            customerAccountRepository.save(customerAccount);
        } catch (UsernameNotFoundException usernameEx) {
            // TODO
        }
    }


    @Override
    public BankAccount loadBankAccountWithId(long bankAccountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow( () -> {
                    throw new BankAccountNotFoundException("Bankaccount mit Nummer: " + bankAccountId + " existiert nicht");
                });

        return bankAccount;
    }

    @Override
    public BankAccount loadBankAccountWithIban(String iban) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findBankAccountByIban(iban)
                .orElseThrow( () -> {
                    throw new BankAccountNotFoundException("Bankaccount mit IBAN: " + iban + " existiert nicht");
                });

        return bankAccount;
    }

    @Override
    @Transactional
    public void archiveBankAccount(BankAccount bankAccount) {
        try {
            bankAccount = loadBankAccountWithId(bankAccount.getBankAccountId());
            bankAccount.setArchived(true);
            bankAccountRepository.save(bankAccount);
        } catch (UsernameNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<BankAccount> getAllActiveBankAccountsFromCustomerAccount(CustomerAccount customerAccount) {
        List<BankAccount> activeBankAccounts = new ArrayList<>();

        customerAccount.getBankAccounts().stream()
                .filter(bankAccount -> !bankAccount.isArchived())
                .forEach(activeBankAccounts::add);

        return activeBankAccounts;
    }

    @Override
    public List<BankAccount> getAllActiveBankAccountsFromCustomerAccountExceptId(CustomerAccount customerAccount,
                                                                                 long exceptionAccountId) {
        List<BankAccount> activeBankAccounts = new ArrayList<>();

        customerAccount.getBankAccounts().stream()
                .filter(bankAccount -> !bankAccount.isArchived() && bankAccount.getBankAccountId() != exceptionAccountId)
                .forEach(activeBankAccounts::add);

        return activeBankAccounts;
    }

    @Override
    public List<SavingsAccount> getAllActiveSavingsAccounts() {
        List<SavingsAccount> savingsAccounts = new ArrayList<>();

        bankAccountRepository.findAll().forEach(account -> {
            if (account instanceof SavingsAccount && !account.isArchived()) {
                savingsAccounts.add((SavingsAccount) account);
            }
        });

        return savingsAccounts;
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
