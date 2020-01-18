package de.marcoedenhofer.edenbank.presentation;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.application.customeraccountservice.GiveawayException;
import de.marcoedenhofer.edenbank.application.customeraccountservice.ICustomerAccountService;
import de.marcoedenhofer.edenbank.application.customeraccountservice.PostIdentException;
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

    @Autowired
    public RegistrationController(ICustomerAccountService registrationService,
                                  IBankAccountService bankAccountService) {
        this.customerAccountService = registrationService;
        this.bankAccountService = bankAccountService;
    }

    @RequestMapping(value = "/create_account", method = RequestMethod.GET)
    public String createAccount(Model model) {
        model.addAttribute("privateCustomer", new PrivateCustomer());
        model.addAttribute("businessCustomer", new BusinessCustomer());
        return "create_account";
    }

    @RequestMapping(value = "/create_account/private", method = RequestMethod.POST)
    public String registerPrivateCustomer(@ModelAttribute("privateCustomer") PrivateCustomer privateCustomer, RedirectAttributes redirectAttributes) {
        CustomerAccount account;

        try {
            account = customerAccountService.createPrivateCustomerAccount(privateCustomer);
            bankAccountService.createCheckingAccountWithFixedBudged(account,100000);
            String accountCreationMessage = "Ihr Konto wurde erfolgreich angelegt. Kundennummer: " + account.getCustomerAccountId();
            redirectAttributes.addFlashAttribute("accountCreationMessage", accountCreationMessage);
            try {
                customerAccountService.callGiveawayService(account);
            } catch (GiveawayException e) {
                redirectAttributes.addFlashAttribute("giveawayServiceError", e.getMessage());
            }
        } catch (PostIdentException e) {
            redirectAttributes.addFlashAttribute("postIdentError", e.getMessage());
        }

        return "redirect:/login";
    }

    @RequestMapping(value = "/create_account/business", method = RequestMethod.POST)
    public String registerBusinessCustomer(@ModelAttribute("businessCustomer") BusinessCustomer businessCustomer, RedirectAttributes redirectAttributes) {
        CustomerAccount account = customerAccountService.createBusinessCustomerAccount(businessCustomer);
        bankAccountService.createCheckingAccountWithFixedBudged(account,100000);
        String accountCreationMessage = "Ihr Konto wurde erfolgreich angelegt. Kundennummer: " + account.getCustomerAccountId();
        redirectAttributes.addFlashAttribute("accountCreationMessage" ,accountCreationMessage);
        try {
            customerAccountService.callGiveawayService(account);
        } catch (GiveawayException e) {
            redirectAttributes.addFlashAttribute("giveawayServiceError", e.getMessage());
        }
        return "redirect:/login";
    }
}
