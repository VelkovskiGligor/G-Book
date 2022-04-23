package mk.ukim.finki.gbook.web.controller;

import mk.ukim.finki.gbook.models.User;
import mk.ukim.finki.gbook.models.exception.InvalidUserCredentialsException;
import mk.ukim.finki.gbook.repository.jpa.UserRepository;
import mk.ukim.finki.gbook.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/login")
public class LoginController {
    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getLoginPage(@RequestParam (required = false) String error,
                               @RequestParam(required = false) String successful,
                               Model model){
        if(successful !=null && !successful.isEmpty()){
            model.addAttribute("hasSuccessful",true);
            model.addAttribute("successful","Your login was successful.");
        }

        return "/login";
    }
    @PostMapping
    public String login(HttpServletRequest request, Model model){
        User user=null;
        try{
            user=this.userService.login(request.getParameter("username"),request.getParameter("password"));
            request.getSession().setAttribute("user",user);
            return "redirect:/books";
        }catch (InvalidUserCredentialsException exception){
            return "/login";
        }

    }
}
