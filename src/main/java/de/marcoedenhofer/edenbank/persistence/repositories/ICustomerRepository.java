package de.marcoedenhofer.edenbank.persistence.repositories;

import de.marcoedenhofer.edenbank.persistence.entities.Customer;
import org.springframework.data.repository.CrudRepository;

public interface ICustomerRepository extends CrudRepository<Customer, Long> {
    Iterable<Customer> findAllByIsIdentifiedTrue();
}
