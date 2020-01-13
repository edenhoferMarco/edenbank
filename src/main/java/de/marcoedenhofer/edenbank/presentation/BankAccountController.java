package de.marcoedenhofer.edenbank.presentation;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.application.customeraccountservice.ICustomerAccountService;
import de.marcoedenhofer.edenbank.application.transactionservice.BankTransactionException;
import de.marcoedenhofer.edenbank.application.transactionservice.ITransactionService;
import de.marcoedenhofer.edenbank.application.transactionservice.TransactionData;
import de.marcoedenhofer.edenbank.persistence.entities.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class BankAccountController {
    private final IBankAccountService bankAccountService;
    private final ICustomerAccountService customerAccountService;
    private final ITransactionService transactionService;

    public BankAccountController(IBankAccountService bankAccountService,
                                 ICustomerAccountService customerAccountService,
                                 ITransactionService transactionService) {
        this.bankAccountService = bankAccountService;
        this.customerAccountService = customerAccountService;
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/bank_account/create/checking_account", method = RequestMethod.POST)
    public String createCheckingAccount(@ModelAttribute("customerAccount") CustomerAccount customerAccount, RedirectAttributes redirectAttributes) {
        bankAccountService.createCheckingAccountForCustomerAccount(customerAccount);
        return "redirect:/overview";
    }

    @RequestMapping(value = "/bank_account/create/savings_account", method = RequestMethod.POST)
    public String createSavingsAccount(@ModelAttribute("customerAccount") CustomerAccount customerAccount, RedirectAttributes redirectAttributes) {
        bankAccountService.createSavingsAccountForCustomerAccount(customerAccount);
        return "redirect:/overview";
    }

    @RequestMapping(value = "/bank_account/create/fixed_deposit_account", method = RequestMethod.POST)
    public String createFixedDepositAccount(@ModelAttribute("customerAccount") CustomerAccount customerAccount, RedirectAttributes redirectAttributes) {
        bankAccountService.createFixedDepositAccountForCustomerAccount(customerAccount);
        return "redirect:/overview";
    }

    @RequestMapping(value = "/bank_account/archive", method = RequestMethod.POST)
    public String registerTransaction(@ModelAttribute("transaction") TransactionData transactionData,
                                      RedirectAttributes redirectAttributes) {
        try {
            transactionService.requestInternalTransaction(transactionData);
            BankAccount bankAccount = bankAccountService.loadBankAccountWithIban(transactionData
                    .getSenderIban());
            bankAccountService.archiveBankAccount(bankAccount);
        } catch (BankTransactionException e) {
            // TODO
            e.printStackTrace();
        }
        return "redirect:/overview";
    }

    @RequestMapping("/bank_account/{bankAccountId}")
    public String getBankAccountDetails(Model model,
                                        @PathVariable("bankAccountId") long bankAccountId,
                                        Principal principal) {
        String customerAccountId = principal.getName();
        CustomerAccount customerAccount = customerAccountService.loadCustomerAccountWithId(Long.parseLong(customerAccountId));
        BankAccount bankAccount = bankAccountService.loadBankAccountWithId(bankAccountId);
        Customer customerDetails = customerAccount.getCustomerDetails();

        if (customerAccountService.customerAccountOwnsBankAccountWithId(customerAccount,bankAccountId)) {
            List<BankAccount> activeBankAccounts =
                    bankAccountService.getAllActiveBankAccountsFromCustomerAccountExceptId(customerAccount,bankAccountId);
            Boolean isBusinessCustomer = customerDetails instanceof BusinessCustomer;
            List<Transaction> transactions = transactionService.loadAllTransactionsWithParticipantBankAccount(bankAccount);

            if (isBusinessCustomer) {
                BusinessCustomer businessCustomerDetails = (BusinessCustomer) customerDetails;
                model.addAttribute("businessCustomerDetails", businessCustomerDetails);
            }

            model.addAttribute("bankAccount", bankAccount);
            model.addAttribute("otherBankAccounts", activeBankAccounts);
            model.addAttribute("customerDetails", customerDetails);
            model.addAttribute("isBusinessCustomer", isBusinessCustomer);
            model.addAttribute("transactions", transactions);
            model.addAttribute("transactionData", new TransactionData());

            return "bank_account_details";
        } else {
            return "redirect:/overview";
        }
    }

}
