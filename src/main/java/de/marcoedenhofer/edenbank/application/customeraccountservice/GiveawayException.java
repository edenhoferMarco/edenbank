package de.marcoedenhofer.edenbank.application.customeraccountservice;

public class GiveawayException extends Exception {
    GiveawayException(String errorMessage) {
        super(errorMessage);
    }
}
