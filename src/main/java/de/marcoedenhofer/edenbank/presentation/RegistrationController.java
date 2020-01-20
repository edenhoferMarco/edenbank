package de.marcoedenhofer.edenbank.presentation;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.application.customeraccountservice.GiveawayException;
import de.marcoedenhofer.edenbank.application.customeraccountservice.ICustomerAccountService;
import de.marcoedenhofer.edenbank.application.customeraccountservice.PostIdentException;
import de.marcoedenhofer.edenbank.application.transactionservice.BankTransactionException;
import de.marcoedenhofer.edenbank.application.transactionservice.ITransactionService;
import de.marcoedenhofer.edenbank.application.transactionservice.TransactionData;
import de.marcoedenhofer.edenbank.persistence.entities.BusinessCustomer;
import de.marcoedenhofer.edenbank.persistence.entities.CustomerAccount;
import de.marcoedenhofer.edenbank.persistence.entities.PrivateCustomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegistrationController {
    private final ICustomerAccountService customerAccountService;
    private final IBankAccountService bankAccountService;
    private final ITransactionService transactionService;

    @Autowired
    public RegistrationController(ICustomerAccountService registrationService,
                                  IBankAccountService bankAccountService, ITransactionService transactionService) {
        this.customerAccountService = registrationService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
    }

    @RequestMapping(value = "/create_account", method = RequestMethod.GET)
    public String createAccount(Model model) {
        model.addAttribute("privateCustomer", new PrivateCustomer());
        model.addAttribute("businessCustomer", new BusinessCustomer());
        return "create_account";
    }

    @RequestMapping(value = "/create_account/private", method = RequestMethod.POST)
    public String registerPrivateCustomer(@ModelAttribute("privateCustomer") PrivateCustomer privateCustomer, RedirectAttributes redirectAttributes) {
        try {
            CustomerAccount account = customerAccountService.createPrivateCustomerAccount(privateCustomer);
            createCheckingAccount(redirectAttributes,account);
            callGiveawayServiceForAccount(redirectAttributes, account);
        } catch (PostIdentException e) {
            redirectAttributes.addFlashAttribute("postIdentError", e.getMessage());
        }

        return "redirect:/login";
    }

    @RequestMapping(value = "/create_account/business", method = RequestMethod.POST)
    public String registerBusinessCustomer(@ModelAttribute("businessCustomer") BusinessCustomer businessCustomer, RedirectAttributes redirectAttributes) {
        CustomerAccount account = customerAccountService.createBusinessCustomerAccount(businessCustomer);
        createCheckingAccount(redirectAttributes,account);
        callGiveawayServiceForAccount(redirectAttributes, account);

        return "redirect:/login";
    }

    private void callGiveawayServiceForAccount(RedirectAttributes redirectAttributes, CustomerAccount account) {
        try {
            TransactionData giveawayPayment = customerAccountService.callGiveawayService(account);
            transactionService.requestInternalTransaction(giveawayPayment);
        } catch (GiveawayException e) {
            redirectAttributes.addFlashAttribute("giveawayServiceError", e.getMessage());
        } catch (BankTransactionException e) {
            e.printStackTrace();
        }
    }

    private void createCheckingAccount(RedirectAttributes redirectAttributes, CustomerAccount account) {
        bankAccountService.createCheckingAccountWithFixedBudged(account,100000);
        String accountCreationMessage = "Ihr Konto wurde erfolgreich angelegt. Kundennummer: " + account.getCustomerAccountId();
        redirectAttributes.addFlashAttribute("accountCreationMessage", accountCreationMessage);
    }
}
