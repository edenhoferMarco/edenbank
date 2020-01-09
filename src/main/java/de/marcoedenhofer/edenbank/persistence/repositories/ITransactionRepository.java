package de.marcoedenhofer.edenbank.persistence.repositories;

import de.marcoedenhofer.edenbank.persistence.entities.BankAccount;
import de.marcoedenhofer.edenbank.persistence.entities.Transaction;
import org.springframework.data.repository.CrudRepository;

public interface ITransactionRepository extends CrudRepository<Transaction, Long> {
    Iterable<Transaction> findAllBySenderBankAccountOrReceiverBankAccount(BankAccount senderBankAccount, BankAccount receiverBankAccount);
}
