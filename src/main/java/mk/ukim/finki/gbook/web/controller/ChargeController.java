package mk.ukim.finki.gbook.web.controller;



import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import lombok.extern.java.Log;
import mk.ukim.finki.gbook.models.ChargeRequest;
import mk.ukim.finki.gbook.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log
@Controller
public class ChargeController {

    @Autowired
    StripeService paymentsService;

    @PostMapping("/charge")
    public String charge(@RequestParam Double amount,
                         @RequestParam String stripeToken,
                         @RequestParam String stripeTokenType,
                         @RequestParam String stripeEmail,
                         @RequestParam String name,
                         @RequestParam String surname,
                         @RequestParam String number,
                         @RequestParam String address,
                         Model model) throws StripeException {
        Long am=Math.round(amount) *100 ;
        ChargeRequest chargeRequest=new ChargeRequest(am,stripeToken,stripeEmail);
        chargeRequest.setDescription("For:"+name+" "+surname+",Address: "+address+",Number: "+number);
        chargeRequest.setCurrency(ChargeRequest.Currency.EUR);
        Charge charge = paymentsService.charge(chargeRequest);
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());
        return "redirect:/shopping-cart/finishTransaction";
    }

    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "result";
    }
}