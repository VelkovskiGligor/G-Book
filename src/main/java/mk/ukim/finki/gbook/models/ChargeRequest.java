package mk.ukim.finki.gbook.models;

import lombok.Data;

@Data
public class ChargeRequest {
    public enum Currency {
        EUR, USD;
    }
    private String description;
    private Long amount;
    private Currency currency;
    private String stripeEmail;
    private String stripeToken;

    public ChargeRequest(Long amount, String stripeToken, String stripeEmail) {
        this.amount = amount;
        this.stripeEmail = stripeEmail;
        this.stripeToken = stripeToken;
    }
}
