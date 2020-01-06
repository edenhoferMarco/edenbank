package de.marcoedenhofer.edenbank.persistence.repositories;

import de.marcoedenhofer.edenbank.persistence.entities.BankAccount;
import org.springframework.data.repository.CrudRepository;

public interface IBankAccountRepository extends CrudRepository<BankAccount, Long> {
}
