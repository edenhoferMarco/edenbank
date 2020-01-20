package de.marcoedenhofer.edenbank.application.customeraccountservice;

public class EmailAlreadyInUseException extends Exception {
    EmailAlreadyInUseException(String errorMessage) {
        super(errorMessage);
    }
}
