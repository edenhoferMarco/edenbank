package de.marcoedenhofer.edenbank.application.bankaccountservice;

import de.marcoedenhofer.edenbank.application.customeraccountservice.ICustomerAccountService;
import de.marcoedenhofer.edenbank.persistence.entities.*;
import de.marcoedenhofer.edenbank.persistence.repositories.IBankAccountRepository;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerAccountRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Service
@Scope("singleton")
public class BankAccountService implements IBankAccountService {
    public final static String EDENBANK_IBAN = "DE750300110000000002";
    public final static String BIGBAZAR_IBAN = "DE750300110000000006";

    // constants for every edenbank account
    private final String COUNTRY_CODE = "DE";
    private final int BANK_CODE = 75030011;
    private final String BIC = "BYEBDEM1RBG";

    // constants for CheckingAccount
    private final int OVERDRAFT_LIMIT = 1000;
    private final float TO_INTEREST_RATE = 0.0f;
    private final float HAVE_INTEREST_RATE = 12.5f;
    private final int TRANSACTION_COST = 100;

    // constants for SavingsAccount
    private final float SAVINGS_ACCOUNT_INTEREST_RATE = 1f;

    // constants for FixedDepositAccount
    private final float FIXED_DEPOSIT_INTEREST_RATE = 2f;
    private final int FIXED_DEPOSIT_DURATION_MINUTES = 10;

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
    public void createCheckingAccountForCustomerAccount(CustomerAccount passedAccount) throws UsernameNotFoundException {
        CustomerAccount customerAccount = customerAccountService.loadCustomerAccountWithId(
                passedAccount.getCustomerAccountId());
        CheckingAccount checkingAccount = createCheckingAccount();
        customerAccount.getBankAccounts().add(checkingAccount);
        customerAccountRepository.save(customerAccount);
    }

    @Override
    public void createSavingsAccountForCustomerAccount(CustomerAccount passedAccount) throws UsernameNotFoundException {
        CustomerAccount customerAccount = customerAccountService.loadCustomerAccountWithId(
                passedAccount.getCustomerAccountId());
        SavingsAccount account = createSavingsAccount();
        customerAccount.getBankAccounts().add(account);
        customerAccountRepository.save(customerAccount);
    }

    @Override
    public void createFixedDepositAccountForCustomerAccount(CustomerAccount passedAccount) throws UsernameNotFoundException {
        CustomerAccount customerAccount = customerAccountService.loadCustomerAccountWithId(
                passedAccount.getCustomerAccountId());
        FixedDepositAccount account = createFixedDepositAccount();
        customerAccount.getBankAccounts().add(account);
        customerAccountRepository.save(customerAccount);
    }

    @Override
    public void createCheckingAccountWithFixedBudged(CustomerAccount passedAccount, long budget) throws UsernameNotFoundException {
        CustomerAccount customerAccount = customerAccountService.loadCustomerAccountWithId(
                passedAccount.getCustomerAccountId());
        CheckingAccount checkingAccount = createCheckingAccount();

        checkingAccount.setBalance(budget);
        bankAccountRepository.save(checkingAccount);

        customerAccount.getBankAccounts().add(checkingAccount);
        customerAccountRepository.save(customerAccount);
    }

    @Override
    public BankAccount loadBankAccountWithId(long bankAccountId) throws BankAccountNotFoundException {

        return bankAccountRepository.findById(bankAccountId)
                .orElseThrow( () -> {
                    throw new BankAccountNotFoundException("Bankaccount mit Nummer: " + bankAccountId + " existiert nicht");
                });
    }

    @Override
    public BankAccount loadBankAccountWithIban(String iban) throws BankAccountNotFoundException {

        return bankAccountRepository.findBankAccountByIban(iban)
                .orElseThrow( () -> {
                    throw new BankAccountNotFoundException("Bankaccount mit IBAN: " + iban + " existiert nicht");
                });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
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
    public List<BankAccount> loadAllActiveBankAccountsFromCustomerAccount(CustomerAccount customerAccount) {

        return customerAccount.getBankAccounts().stream()
                .filter(not(BankAccount::isArchived))
                .collect(Collectors.toList());
    }

    @Override
    public List<BankAccount> loadAllActiveBankAccountsFromCustomerAccountExceptId(CustomerAccount customerAccount,
                                                                                  long exceptionAccountId) {

        return customerAccount.getBankAccounts().stream()
                .filter(not(BankAccount::isArchived))
                .filter(bankAccount -> bankAccount.getBankAccountId() != exceptionAccountId)
                .collect(Collectors.toList());
    }

    @Override
    public List<SavingsAccount> loadAllActiveSavingsAccounts() {
        List<SavingsAccount> savingsAccounts = new ArrayList<>();

        bankAccountRepository.findAll().forEach(account -> {
            if (account instanceof SavingsAccount && !account.isArchived()) {
                savingsAccounts.add((SavingsAccount) account);
            }
        });

        return savingsAccounts;
    }

    @Override
    public List<FixedDepositAccount> loadAllActiveFixedDepositAccounts() {
        List<FixedDepositAccount> fixedDepostiAccounts = new ArrayList<>();

        bankAccountRepository.findAll().forEach(account -> {
            if (account instanceof FixedDepositAccount && !account.isArchived()) {
                fixedDepostiAccounts.add((FixedDepositAccount) account);
            }
        });

        return fixedDepostiAccounts;
    }

    private CheckingAccount createCheckingAccount() {
        CheckingAccount account = new CheckingAccount();
        initializeWithConstantValues(account);
        account.setBalance(0);
        account = bankAccountRepository.save(account);
        long bankAccountId = account.getBankAccountId();
        account.setIban(buildIban(bankAccountId));
        account.setHaveInterestRate(HAVE_INTEREST_RATE);
        account.setToInterestRate(TO_INTEREST_RATE);
        account.setTransactionCost(TRANSACTION_COST);
        account.setOverdraftLimit(OVERDRAFT_LIMIT);
        account = bankAccountRepository.save(account);

        return account;
    }

    private SavingsAccount createSavingsAccount() {
        SavingsAccount account = new SavingsAccount();
        initializeWithConstantValues(account);
        account.setBalance(0);
        account = bankAccountRepository.save(account);
        long bankAccountId = account.getBankAccountId();
        account.setIban(buildIban(bankAccountId));
        account.setInterestRate(SAVINGS_ACCOUNT_INTEREST_RATE);
        account = bankAccountRepository.save(account);

        return account;
    }

    private FixedDepositAccount createFixedDepositAccount() {
        FixedDepositAccount account = new FixedDepositAccount();
        initializeWithConstantValues(account);
        account.setBalance(0);
        account = bankAccountRepository.save(account);
        long bankAccountId = account.getBankAccountId();
        account.setIban(buildIban(bankAccountId));
        account.setInterestRate(FIXED_DEPOSIT_INTEREST_RATE);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, FIXED_DEPOSIT_DURATION_MINUTES);
        account.setEndDate(cal.getTime());
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
