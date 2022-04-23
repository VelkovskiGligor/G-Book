package mk.ukim.finki.gbook.web.controller;


import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import mk.ukim.finki.gbook.models.Book;
import mk.ukim.finki.gbook.models.ChargeRequest;
import mk.ukim.finki.gbook.models.ShoppingCart;
import mk.ukim.finki.gbook.models.User;
import mk.ukim.finki.gbook.models.exception.EmptyShoppingCart;
import mk.ukim.finki.gbook.repository.jpa.UserRepository;
import mk.ukim.finki.gbook.service.BooksService;
import mk.ukim.finki.gbook.service.ShoppingCartService;
import mk.ukim.finki.gbook.service.StripeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/shopping-cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;
    private final BooksService booksService;
    private final UserRepository userRepository;

    private final StripeService paymentsService;


    @Value("${STRIPE_PUBLIC_KEY}")
    private String stripePublicKey;

    public ShoppingCartController(ShoppingCartService shoppingCartService, BooksService booksService, UserRepository userRepository, StripeService paymentsService) {
        this.shoppingCartService = shoppingCartService;
        this.booksService = booksService;
        this.userRepository = userRepository;
        this.paymentsService = paymentsService;
    }

    @GetMapping
    public String getShoppingCartPage(@RequestParam(required = false) String error, HttpServletRequest request, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        String username = request.getRemoteUser();
        User user = this.userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(username);
        List<Book> books = shoppingCartService.listAllBooksInShoppingCart(shoppingCart.getId());
        this.shoppingCartService.checkSum(books,shoppingCart.getId());
        Double totalPrice = shoppingCart.getTotalPrice()+20;
        model.addAttribute("user", user);
        model.addAttribute("books", books);
        model.addAttribute("totalPrice", Math.round(totalPrice));
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.USD);
        model.addAttribute("title","Shopping cart");
        model.addAttribute("bodyContent","shopping-cart");
        return "master-template";
    }

    @GetMapping("/finishTransaction")
    public String finishTransaction(Model model,HttpServletRequest request)
             {

             Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
               //  String currentPrincipalName = authentication.getName();
                 String username = authentication.getName();
         ShoppingCart shoppingCart = this.shoppingCartService.getActiveShoppingCart(username);
        try {
            this.shoppingCartService.setFinish(shoppingCart.getId());
        } catch (EmptyShoppingCart ex) {
            return "redirect:/shopping-cart?empty=true";
        }
        return "redirect:/shopping-cart?success=true";


    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "result";
    }

    @GetMapping("add-book/{id}")
    public String addBookToShoppingCart(@PathVariable Long id, HttpServletRequest request) {
        try {
            String username = request.getRemoteUser();
            Book book = this.booksService.findById(id).get();
            if (book.getQuantity() == 0) {
                this.booksService.deleteById(id);
                return "redirect:/books?error=there+are+no+more+items+in+this+book";
            }
            book.setQuantity(book.getQuantity() - 1);
            ShoppingCart shoppingCart = this.shoppingCartService.addProductToShoppingCart(username, id);
            return "redirect:/shopping-cart";

        } catch (RuntimeException ex) {
            return "redirect:/shopping-cart?error=" + ex.getMessage();
        }
    }

    @DeleteMapping("/delete-book/{id}")
    public String deleteBookFromShoppingCart(@PathVariable Long id, HttpServletRequest request) {
        this.shoppingCartService.deleteProductInShoppingCart(request.getRemoteUser(), id);
        return "redirect:/shopping-cart";
    }

}
