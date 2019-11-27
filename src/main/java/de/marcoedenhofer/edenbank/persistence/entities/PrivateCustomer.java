package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Embeddable
@Inheritance(strategy = InheritanceType.JOINED)
public class PrivateCustomer extends Customer {
}
