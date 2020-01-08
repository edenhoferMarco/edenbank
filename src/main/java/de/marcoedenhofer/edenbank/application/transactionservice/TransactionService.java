package de.marcoedenhofer.edenbank.application.transactionservice;

import de.marcoedenhofer.edenbank.application.bankaccountservice.BankAccountNotFoundException;
import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.persistence.entities.BankAccount;
import de.marcoedenhofer.edenbank.persistence.entities.Transaction;
import de.marcoedenhofer.edenbank.persistence.repositories.IBankAccountRepository;
import de.marcoedenhofer.edenbank.persistence.repositories.ITransactionRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TransactionService implements ITransactionService {

    private Queue<Transaction> transactionQueue;

    private final ITransactionRepository transactionRepository;
    private final IBankAccountRepository bankAccountRepository;
    private  final IBankAccountService bankAccountService;

    TransactionService(ITransactionRepository transactionRepository,
                       IBankAccountRepository bankAccountRepository,
                       IBankAccountService bankAccountService) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
        this.transactionQueue = new PriorityQueue<>();
    }

    @Override
    public void requestTransaction(TransactionData transactionData) throws BankTransactionException {
        try {
            BankAccount sender = bankAccountService.loadBankAccountWithIban(transactionData.getSenderIban());
            BankAccount receiver = bankAccountService.loadBankAccountWithIban(transactionData.getReceiverIban());
            Transaction transaction = new Transaction();
            transaction.setAmount(transactionData.getAmount());
            transaction.setSenderBankAccount(sender);
            transaction.setReceiverBankAccount(receiver);
            transaction.setUsageDetails(transactionData.getUsageDetails());

            // Debug only
            executeTransaction(transaction);

        } catch (UsernameNotFoundException ex) {
            throw new BankTransactionException(ex.getMessage());
        }
    }

    @Override
    public void requestTransaction(Transaction transaction) throws BankTransactionException {
        try {
            transactionQueue.add(transaction);
            // Debug only
            executeTransactions();
        } catch (UsernameNotFoundException ex) {
            throw new BankTransactionException(ex.getMessage());
        }
    }

    @Override
    public List<Transaction> loadAllTransactionsWithParticipantIban(String participantIban) {
        List<Transaction> participatedTransactions = new ArrayList<>();
        Iterator transactionIterator = transactionRepository.findAll().iterator();
        transactionIterator.forEachRemaining((t) -> {
            if (t instanceof Transaction) {
                Transaction transaction = (Transaction) t;
                String senderIban = transaction.getSenderBankAccount().getIban();
                String receiverIban = transaction.getReceiverBankAccount().getIban();
                if (senderIban.equals(participantIban) || receiverIban.equals(participantIban)) {
                    participatedTransactions.add(transaction);
                }
            }
        });

        return participatedTransactions;
    }

    protected void executeTransactions() {
        for (Transaction transaction : transactionQueue) {
            if (!transaction.isTransactionDone()) {
                executeTransaction(transaction);
            }
        }
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
}
