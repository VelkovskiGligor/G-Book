package mk.ukim.finki.gbook.web.controller;

import mk.ukim.finki.gbook.models.User;
import mk.ukim.finki.gbook.models.enumeration.Role;
import mk.ukim.finki.gbook.models.exception.InvalidArgumentException;
import mk.ukim.finki.gbook.models.exception.PasswordDoNotMatchException;
import mk.ukim.finki.gbook.models.exception.UsernameAlreadyExistsException;
import mk.ukim.finki.gbook.service.AuthorService;
import mk.ukim.finki.gbook.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getProfilePage(Model model, HttpServletRequest request){
        String username=request.getRemoteUser();
        User user=(User) userService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("title","Profile");
        model.addAttribute("bodyContent","profile");
        return "master-template";
    }
    @GetMapping("/edit")
    public String editProfile(Model model, HttpServletRequest request){
        String username=request.getRemoteUser();
        User user=(User) userService.loadUserByUsername(username);
        model.addAttribute("user",user);
        model.addAttribute("title","Edit Profile");
        model.addAttribute("bodyContent","profile-edit");
        return "master-template";
    }

    @PostMapping
    public String saveInfo(
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam String address,
                           @RequestParam String number,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           HttpServletRequest request
                            ) {
        try {
            if (name == "" || surname == "" || address == "" || number == "" || password == "" || repeatedPassword == ""  ) {
                return "redirect:/profile/edit?hasErrorRequired=true&&errorRequired=Required*";
            }
            String username=request.getRemoteUser();
            User user= (User)userService.loadUserByUsername(username);
            this.userService.edit( username,  password,  repeatedPassword,  name,  surname,  number,  address, user.getRole() );
            return "redirect:/profile";
        } catch (UsernameAlreadyExistsException | InvalidArgumentException | PasswordDoNotMatchException ex) {
            return "redirect:/profile/edit?hasError=true&&error=" + ex.getMessage();
        }

    }

}
