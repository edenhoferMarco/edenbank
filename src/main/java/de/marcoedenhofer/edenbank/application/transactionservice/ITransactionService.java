package de.marcoedenhofer.edenbank.application.transactionservice;

import de.marcoedenhofer.edenbank.persistence.entities.BankAccount;
import de.marcoedenhofer.edenbank.persistence.entities.Transaction;

import java.util.List;

public interface ITransactionService {
    void requestTransaction(TransactionData transactionData) throws BankTransactionException;
    void requestTransaction(Transaction transaction) throws BankTransactionException;
    List<Transaction> loadAllTransactionsWithParticipantBankAccount(BankAccount bankAccount);
}
