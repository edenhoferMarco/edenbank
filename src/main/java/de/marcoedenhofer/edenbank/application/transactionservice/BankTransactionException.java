package de.marcoedenhofer.edenbank.application.transactionservice;

public class BankTransactionException extends Exception {

    private String errorMessage;

    BankTransactionException(String errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
