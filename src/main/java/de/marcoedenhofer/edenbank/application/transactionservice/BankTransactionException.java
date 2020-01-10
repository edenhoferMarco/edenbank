package de.marcoedenhofer.edenbank.application.transactionservice;

public class BankTransactionException extends Exception {
    BankTransactionException(String errorMessage) {
        super(errorMessage);
    }
}
