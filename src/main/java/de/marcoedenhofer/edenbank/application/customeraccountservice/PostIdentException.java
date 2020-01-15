package de.marcoedenhofer.edenbank.application.customeraccountservice;

public class PostIdentException extends Exception {
    PostIdentException(String errorMessage) {
        super(errorMessage);
    }
}
