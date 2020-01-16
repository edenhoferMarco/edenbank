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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @RequestMapping(value = "apis/transaction/execute", method = RequestMethod.POST)
    @ResponseBody
    public TransactionData executeTransaction(@RequestBody TransactionData transactionData) throws BankTransactionException {

        return transactionService.requestTransaction(transactionData);
    }

    @RequestMapping(value = "/transaction", method = RequestMethod.GET)
    public String createNewTransaction(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        CustomerAccount customerAccount = customerAccountService.loadCustomerAccountWithId(auth.getName());
        List<BankAccount> activeBankAccounts = bankAccountService.getAllActiveBankAccountsFromCustomerAccount(customerAccount);

        model.addAttribute("bankAccounts", activeBankAccounts);
        model.addAttribute("transactionData", new TransactionData());

        return "transaction";
    }

    @RequestMapping(value = "/transaction/execute", method = RequestMethod.POST)
    public String executeNewTransaction(@ModelAttribute TransactionData transactionData,
                                        RedirectAttributes redirectAttributes) {
        try {
            transactionService.requestInternalTransaction(transactionData);
            String successMessage = "Auf Konto: " + transactionData.getReceiverIban() + " wurden "
                    + transactionData.getAmount() +" € von Konto: " + transactionData.getSenderIban() + " überwiesen.";
            redirectAttributes.addFlashAttribute("transactionSuccess", successMessage);
        } catch (BankTransactionException e) {
            redirectAttributes.addFlashAttribute("transactionError", e.getMessage());
        }

        return "redirect:/transaction";
    }
}
