package de.marcoedenhofer.edenbank.presentation;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.persistence.entities.BusinessCustomer;
import de.marcoedenhofer.edenbank.persistence.entities.CustomerAccount;
import de.marcoedenhofer.edenbank.persistence.repositories.IBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BankAccountController {
    private final IBankAccountService bankAccountService;

    public BankAccountController(IBankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
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


}
