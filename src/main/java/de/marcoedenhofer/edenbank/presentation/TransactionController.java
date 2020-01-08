package de.marcoedenhofer.edenbank.presentation;

import de.marcoedenhofer.edenbank.application.transactionservice.BankTransactionException;
import de.marcoedenhofer.edenbank.application.transactionservice.ITransactionService;
import de.marcoedenhofer.edenbank.application.transactionservice.TransactionData;
import de.marcoedenhofer.edenbank.persistence.entities.CustomerAccount;
import de.marcoedenhofer.edenbank.persistence.entities.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
public class TransactionController {
    private final ITransactionService transactionService;

    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/transaction/register", method = RequestMethod.POST)
    public String registerTransaction(@ModelAttribute("transaction") Transaction transaction,
                                      RedirectAttributes redirectAttributes) {

        try {
            transactionService.requestTransaction(transaction);
        } catch (BankTransactionException e) {
            // TODO
            e.printStackTrace();
        }
        return "redirect:/overview";
    }
}
