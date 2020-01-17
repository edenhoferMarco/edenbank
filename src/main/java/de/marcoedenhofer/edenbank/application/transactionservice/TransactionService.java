package de.marcoedenhofer.edenbank.application.transactionservice;

import de.marcoedenhofer.edenbank.application.bankaccountservice.BankAccountNotFoundException;
import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.persistence.entities.BankAccount;
import de.marcoedenhofer.edenbank.persistence.entities.FixedDepositAccount;
import de.marcoedenhofer.edenbank.persistence.entities.Transaction;
import de.marcoedenhofer.edenbank.persistence.repositories.IBankAccountRepository;
import de.marcoedenhofer.edenbank.persistence.repositories.ITransactionRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Scope("singleton")
public class TransactionService implements ITransactionService {
    private final ITransactionRepository transactionRepository;
    private final IBankAccountRepository bankAccountRepository;
    private final IBankAccountService bankAccountService;
    private final AuthenticationManager authenticationManager;

    TransactionService(ITransactionRepository transactionRepository,
                       IBankAccountRepository bankAccountRepository,
                       IBankAccountService bankAccountService,
                       AuthenticationManager authenticationManager) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public TransactionData requestTransaction(TransactionData transactionData) throws BankTransactionException {
        try {
            BankAccount sender = bankAccountService.loadBankAccountWithIban(transactionData.getSenderIban());
            BankAccount receiver = bankAccountService.loadBankAccountWithIban(transactionData.getReceiverIban());

            if (!canAuthenticate(transactionData.getSenderCustomerAccountId(),
                    transactionData.getSenderPassword())) {
                throw new BankTransactionException("Falsches Passwort");
            }

            Transaction transaction = buildTransaction(transactionData,sender, receiver);
            executeTransaction(transaction);
            transactionData.setSenderCustomerAccountId(0);
            transactionData.setSenderPassword("");

            return transactionData;
        } catch (UsernameNotFoundException ex) {
            throw new BankTransactionException(ex.getMessage());
        } catch (AuthenticationException ex) {
            throw new BankTransactionException("Falsches Passwort");
        }
    }

    @Override
    public void requestInternalTransaction(TransactionData transactionData) throws BankTransactionException {
        try {
            BankAccount sender = bankAccountService.loadBankAccountWithIban(transactionData.getSenderIban());
            if (sender instanceof FixedDepositAccount && !(((FixedDepositAccount) sender).isDone())) {
                throw new BankTransactionException("Festgeldkonto: " + sender.getIban()
                        + " ist noch gesperrt. Stillegung nicht möglich");
            }
            BankAccount receiver = bankAccountService.loadBankAccountWithIban(transactionData.getReceiverIban());
            Transaction transaction = buildTransaction(transactionData,sender, receiver);

            executeTransaction(transaction);
        } catch (BankAccountNotFoundException ex) {
            throw new BankTransactionException(ex.getMessage());
        }
    }

    @Override
    public List<Transaction> loadAllTransactionsWithParticipatingBankAccount(BankAccount bankAccount) {
        List<Transaction> participatedTransactions = new ArrayList<>();
        Iterable<Transaction> transactions = transactionRepository.
                findAllBySenderBankAccountOrReceiverBankAccountOrderByTransactionDateDesc(bankAccount, bankAccount);
        transactions.forEach(participatedTransactions::add);

        return participatedTransactions;
    }

    private Transaction buildTransaction(TransactionData transactionData, BankAccount sender, BankAccount receiver) {
        Transaction transaction = new Transaction();

        int amountInInteger = makeIntegerFromDouble(transactionData.getAmount());
        transaction.setAmount(amountInInteger);
        transaction.setSenderBankAccount(sender);
        transaction.setReceiverBankAccount(receiver);
        transaction.setUsageDetails(transactionData.getUsageDetails());

        return transaction;
    }

    @Transactional
    protected void executeTransaction(Transaction transaction) throws BankTransactionException {
        try {
            BankAccount senderAccount = bankAccountService.loadBankAccountWithId(
                    transaction.getSenderBankAccount().getBankAccountId());
            BankAccount receiverAccount = bankAccountService.loadBankAccountWithId(
                    transaction.getReceiverBankAccount().getBankAccountId());

            if (senderAccount.isArchived() || receiverAccount.isArchived()) {
                throw new BankTransactionException("Transaktion nicht möglich, Bankkonto ist stillgelegt");
            }
            if (senderAccount.getBalance() - transaction.getAmount() < senderAccount.getOverdraftLimit()) {
                throw new BankTransactionException("Konto " + senderAccount.getIban() +
                        " hat kein Geld mehr zur Verfügung!");
            }

            transaction.setTransactionDate(Calendar.getInstance().getTime());
            senderAccount.setBalance(senderAccount.getBalance() - transaction.getAmount());
            receiverAccount.setBalance((receiverAccount.getBalance() + transaction.getAmount()));

            bankAccountRepository.save(senderAccount);
            bankAccountRepository.save(receiverAccount);
            transaction.setTransactionDone(true);
            transactionRepository.save(transaction);
        } catch (UsernameNotFoundException ex) {
            throw new BankTransactionException(ex.getMessage());
        }

    }

    private boolean canAuthenticate(long customerAccountId, String password) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                customerAccountId, password);

        return authenticationManager.authenticate(authToken).isAuthenticated();
    }

    private int makeIntegerFromDouble(double input) {
        Double conversionValue = input * 100;
        return conversionValue.intValue();
    }
}
