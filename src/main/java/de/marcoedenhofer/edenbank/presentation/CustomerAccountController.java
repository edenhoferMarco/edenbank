package de.marcoedenhofer.edenbank.presentation;

import de.marcoedenhofer.edenbank.application.registrationservice.IRegistrationService;
import de.marcoedenhofer.edenbank.persistence.entities.Customer;
import de.marcoedenhofer.edenbank.persistence.entities.CustomerAccount;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Scope("session")
public class CustomerAccountController {
    private final IRegistrationService registrationService;
    private final UserDetailsService userDetailsService;


    public CustomerAccountController(IRegistrationService registrationService,
                                     @Qualifier("security")UserDetailsService userDetailsService) {
        this.registrationService = registrationService;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "/account")
    public String account(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails details = userDetailsService.loadUserByUsername(auth.getName());
        if (details instanceof CustomerAccount) {
            CustomerAccount cust = (CustomerAccount) details;
            Customer customerDetails = cust.getCustomerDetails();
            model.addAttribute("customerAccount", cust);
            model.addAttribute("customerDetails", customerDetails);
        }

        return "account";
    }
}
