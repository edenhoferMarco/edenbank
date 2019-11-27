package de.marcoedenhofer.edenbank.persistence.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bankAccountId;
    private int bankCode;
    private String iban;
    private String bic;
    private double balance;
    private boolean isArchived;
}
