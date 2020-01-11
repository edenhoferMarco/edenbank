package de.marcoedenhofer.edenbank.presentation;

import de.marcoedenhofer.edenbank.application.bankaccountservice.IBankAccountService;
import de.marcoedenhofer.edenbank.application.customeraccountservice.ICustomerAccountService;
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
    private final ICustomerAccountService registrationService;
    private final IBankAccountService bankAccountService;

    @Autowired
    public RegistrationController(ICustomerAccountService registrationService,
                                  IBankAccountService bankAccountService) {
        this.registrationService = registrationService;
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
        CustomerAccount account = registrationService.createPrivateCustomerAccount(privateCustomer);
        bankAccountService.createCheckingAccountForCustomerAccount(account);
        return "redirect:/login";
    }

    @RequestMapping(value = "/create_account/business", method = RequestMethod.POST)
    public String registerBusinessCustomer(@ModelAttribute("businessCustomer") BusinessCustomer businessCustomer, RedirectAttributes redirectAttributes) {
        this.registrationService.createBusinessCustomerAccount(businessCustomer);
        return "redirect:/login";
    }
}
