package de.marcoedenhofer.edenbank.persistence.repositories;

import de.marcoedenhofer.edenbank.persistence.entities.CustomerAccount;
import org.springframework.data.repository.CrudRepository;

public interface ICustomerAccountRepository extends CrudRepository<CustomerAccount, Long> {
    long count();
}
