package de.marcoedenhofer.edenbank.application.transactionservice;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.application.registrationservice.IRegistrationService;
import de.marcoedenhofer.edenbank.persistence.entities.BankAccount;
import de.marcoedenhofer.edenbank.persistence.entities.Transaction;
import de.marcoedenhofer.edenbank.persistence.repositories.IBankAccountRepository;
import de.marcoedenhofer.edenbank.persistence.repositories.ITransactionRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TransactionService implements ITransactionService {
    private final ITransactionRepository transactionRepository;
    private final IBankAccountRepository bankAccountRepository;
    private final IBankAccountService bankAccountService;
    private final IRegistrationService registrationService;
    private final AuthenticationManager authenticationManager;

    TransactionService(ITransactionRepository transactionRepository,
                       IBankAccountRepository bankAccountRepository,
                       IBankAccountService bankAccountService,
                       IRegistrationService registrationService,
                       AuthenticationManager authenticationManager) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
        this.registrationService = registrationService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void requestTransaction(TransactionData transactionData) throws BankTransactionException {
        try {
            BankAccount sender = bankAccountService.loadBankAccountWithIban(transactionData.getSenderIban());
            BankAccount receiver = bankAccountService.loadBankAccountWithIban(transactionData.getReceiverIban());

            if (!isAuthenticatable(transactionData.getSenderCustomerAccountId(),
                    transactionData.getSenderPassword())) {
                throw new BankTransactionException("Falsches Passwort");
            }

            Transaction transaction = new Transaction();
            transaction.setAmount(transactionData.getAmount());
            transaction.setSenderBankAccount(sender);
            transaction.setReceiverBankAccount(receiver);
            transaction.setUsageDetails(transactionData.getUsageDetails());

            executeTransaction(transaction);
        } catch (UsernameNotFoundException ex) {
            throw new BankTransactionException(ex.getMessage());
        } catch (AuthenticationException ex) {
            throw new BankTransactionException("Falsches Passwort");
        }
    }

    @Override
    public void requestTransaction(Transaction transaction) throws BankTransactionException {
        try {
            BankAccount sender = bankAccountService.loadBankAccountWithIban(transaction.getSenderBankAccount().getIban());
            BankAccount receiver = bankAccountService.loadBankAccountWithIban(transaction.getReceiverBankAccount().getIban());
            transaction.setSenderBankAccount(sender);
            transaction.setReceiverBankAccount(receiver);

            executeTransaction(transaction);
        } catch (UsernameNotFoundException ex) {
            throw new BankTransactionException(ex.getMessage());
        }
    }

    @Override
    public List<Transaction> loadAllTransactionsWithParticipantBankAccount(BankAccount bankAccount) {
        List<Transaction> participatedTransactions = new ArrayList<>();
        Iterable<Transaction> transactions = transactionRepository.findAllBySenderBankAccountOrReceiverBankAccount(bankAccount, bankAccount);
        transactions.forEach(participatedTransactions::add);

        return participatedTransactions;
    }

    @Transactional
    protected void executeTransaction(Transaction transaction) throws UsernameNotFoundException {
        BankAccount senderAccount = bankAccountService.loadBankAccountWithId(
                transaction.getSenderBankAccount().getBankAccountId());
        BankAccount receiverAccount = bankAccountService.loadBankAccountWithId(
                transaction.getReceiverBankAccount().getBankAccountId());

        senderAccount.setBalance(senderAccount.getBalance() - transaction.getAmount());
        receiverAccount.setBalance((receiverAccount.getBalance() + transaction.getAmount()));

        bankAccountRepository.save(senderAccount);
        bankAccountRepository.save(receiverAccount);
        transaction.setTransactionDone(true);
        transactionRepository.save(transaction);
    }

    private boolean isAuthenticatable(long customerAccountId, String password) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customerAccountId, password);

        return authenticationManager.authenticate(authToken).isAuthenticated();
    }
}
