package de.marcoedenhofer.edenbank.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController {

    @RequestMapping("/")
    public String index() {
        return "redirect:/overview";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }
}
