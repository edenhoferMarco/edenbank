package de.marcoedenhofer.edenbank.persistence.repositories;

import de.marcoedenhofer.edenbank.persistence.entities.BankAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IBankAccountRepository extends CrudRepository<BankAccount, Long> {
    Optional<BankAccount> findBankAccountByIban(String iban);
}
