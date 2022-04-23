package mk.ukim.finki.gbook.web.controller;

import mk.ukim.finki.gbook.models.enumeration.Role;
import mk.ukim.finki.gbook.models.exception.InvalidArgumentException;
import mk.ukim.finki.gbook.models.exception.PasswordDoNotMatchException;
import mk.ukim.finki.gbook.models.exception.UsernameAlreadyExistsException;
import mk.ukim.finki.gbook.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error,
                                  @RequestParam(required = false) String errorRequired,
                                  Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        if (errorRequired != null && !errorRequired.isEmpty()) {
            model.addAttribute("hasErrorRequired", true);
            model.addAttribute("errorRequired", errorRequired);
        }

        return "register";
    }

    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String address,
                           @RequestParam String number,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam (required = false) Role role) {
        try {
            if (username == "" || name == "" || surname == "" || address == "" || number == "" || password == "" || repeatedPassword == "" || role ==null) {
                return "redirect:/register?hasErrorRequired=true&&errorRequired=Required*";
            }
            this.userService.register(username, name, surname, address, number, password, repeatedPassword, role);
            return "redirect:/login?hasSuccessful=true&&successful=successful";
        } catch (UsernameAlreadyExistsException | InvalidArgumentException | PasswordDoNotMatchException ex) {
            return "redirect:/register?hasError=true&&error=" + ex.getMessage();
        }

    }


}
