package de.marcoedenhofer.edenbank.presentation;

import de.marcoedenhofer.edenbank.application.customeraccountservice.ICustomerAccountService;
import de.marcoedenhofer.edenbank.persistence.entities.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@Scope("session")
public class CustomerAccountController {
    private final ICustomerAccountService registrationService;
    private final UserDetailsService userDetailsService;


    public CustomerAccountController(ICustomerAccountService registrationService,
                                     @Qualifier("security")UserDetailsService userDetailsService) {
        this.registrationService = registrationService;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "/overview")
    public String account(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails details = userDetailsService.loadUserByUsername(auth.getName());
        if (details instanceof CustomerAccount) {
            CustomerAccount customerAccount = (CustomerAccount) details;
            Customer customerDetails = customerAccount.getCustomerDetails();
            List<BankAccount> bankAccounts = customerAccount.getBankAccounts();
            List<CheckingAccount> checkingAccounts = collectAllCheckingAccounts(bankAccounts);
            List<SavingsAccount> savingsAccounts = collectAllSavingsAccounts(bankAccounts);
            List<FixedDepositAccount> fixedDepositAccounts = collectAllFixedDepositAccounts(bankAccounts);

            model.addAttribute("customerAccount", customerAccount);
            model.addAttribute("checkingAccounts", checkingAccounts);
            model.addAttribute("savingsAccounts", savingsAccounts);
            model.addAttribute("fixedDepositAccounts", fixedDepositAccounts);
            model.addAttribute("customerDetails", customerDetails);
        }

        return "overview";
    }

    private List<CheckingAccount> collectAllCheckingAccounts(List<BankAccount> bankAccounts) {
        List<CheckingAccount> checkingAccounts = new ArrayList<>();
        bankAccounts.forEach(bankAccount -> {
            if (!bankAccount.isArchived() && bankAccount instanceof CheckingAccount) {
                checkingAccounts.add((CheckingAccount) bankAccount);
            }
        });

        return checkingAccounts;
    }

    private List<SavingsAccount> collectAllSavingsAccounts(List<BankAccount> bankAccounts) {
        List<SavingsAccount> savingsAccounts = new ArrayList<>();
        bankAccounts.forEach(bankAccount -> {
            if (!bankAccount.isArchived() && bankAccount instanceof SavingsAccount) {
                savingsAccounts.add((SavingsAccount) bankAccount);
            }
        });

        return savingsAccounts;
    }

    private List<FixedDepositAccount> collectAllFixedDepositAccounts(List<BankAccount> bankAccounts) {
        List<FixedDepositAccount> fixedDepositAccounts = new ArrayList<>();
        bankAccounts.forEach(bankAccount -> {
            if (!bankAccount.isArchived() && bankAccount instanceof FixedDepositAccount) {
                fixedDepositAccounts.add((FixedDepositAccount) bankAccount);
            }
        });

        return fixedDepositAccounts;
    }
}
