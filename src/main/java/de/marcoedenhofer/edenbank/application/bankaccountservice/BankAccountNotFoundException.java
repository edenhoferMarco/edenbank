package de.marcoedenhofer.edenbank.application.bankaccountservice;

public class BankAccountNotFoundException extends RuntimeException {
    BankAccountNotFoundException(String message) {
        super(message);
    }
}
