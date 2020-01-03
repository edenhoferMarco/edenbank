package de.marcoedenhofer.edenbank.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegistrationController {
    @RequestMapping("/create_account")
    public String createAccount() {
        return "create_account";
    }
}
