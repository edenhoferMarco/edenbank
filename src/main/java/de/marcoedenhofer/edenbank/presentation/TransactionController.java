package de.marcoedenhofer.edenbank.presentation;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.application.customeraccountservice.ICustomerAccountService;
import de.marcoedenhofer.edenbank.application.transactionservice.BankTransactionException;
import de.marcoedenhofer.edenbank.application.transactionservice.ITransactionService;
import de.marcoedenhofer.edenbank.application.transactionservice.TransactionData;

import de.marcoedenhofer.edenbank.persistence.entities.BankAccount;
import de.marcoedenhofer.edenbank.persistence.entities.CustomerAccount;
import de.marcoedenhofer.edenbank.persistence.entities.Transaction;
import de.marcoedenhofer.edenbank.persistence.repositories.ICustomerAccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TransactionController {
    private final ITransactionService transactionService;
    private final ICustomerAccountService customerAccountService;
    private final IBankAccountService bankAccountService;

    public TransactionController(ITransactionService transactionService,
                                 ICustomerAccountService customerAccountService,
                                 IBankAccountService bankAccountService) {
        this.transactionService = transactionService;
        this.customerAccountService = customerAccountService;
        this.bankAccountService = bankAccountService;
    }

    @RequestMapping(value = "/transactionapi/execute", method = RequestMethod.POST)
    @ResponseBody
    public TransactionData executeTransaction(@RequestBody TransactionData transactionData) throws BankTransactionException {
        TransactionData response = transactionService.requestTransaction(transactionData);

        return response;
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public String createNewTransaction(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerAccount customerAccount = customerAccountService.loadCustomerAccountWithId(auth.getName());
        List<BankAccount> activeBankAccounts = bankAccountService.getAllActiveBankAccountsFromCustomerAccount(customerAccount);

        model.addAttribute("bankAccounts", activeBankAccounts);
        model.addAttribute("transaction", new Transaction());

        return "transaction";
    }

    @RequestMapping(value = "/transaction/execute", method = RequestMethod.POST)
    public String executeNewTransaction(@ModelAttribute Transaction transaction) {

        return "redirect:overview";
    }
}
