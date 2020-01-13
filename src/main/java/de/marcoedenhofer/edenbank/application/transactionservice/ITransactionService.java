package de.marcoedenhofer.edenbank.application.transactionservice;

import de.marcoedenhofer.edenbank.persistence.entities.BankAccount;
import de.marcoedenhofer.edenbank.persistence.entities.Transaction;

import java.util.List;

public interface ITransactionService {
    TransactionData requestTransaction(TransactionData transactionData) throws BankTransactionException;
    void requestInternalTransaction(TransactionData transactionData) throws BankTransactionException;
    List<Transaction> loadAllTransactionsWithParticipantBankAccount(BankAccount bankAccount);
}
