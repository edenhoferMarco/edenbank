package de.marcoedenhofer.edenbank.presentation;

import de.marcoedenhofer.edenbank.application.transactionservice.BankTransactionException;
import de.marcoedenhofer.edenbank.application.transactionservice.ITransactionService;
import de.marcoedenhofer.edenbank.application.transactionservice.TransactionData;

import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {
    private final ITransactionService transactionService;

    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/transactionapi/execute")
    public TransactionData executeTransaction(@RequestBody TransactionData transactionData) throws BankTransactionException {
        TransactionData response = transactionService.requestTransaction(transactionData);

        return response;
    }
}
