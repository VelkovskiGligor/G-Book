package mk.ukim.finki.gbook.models.exception;

public class EmptyShoppingCart extends RuntimeException {
    public EmptyShoppingCart(String message) {
        super(message);
    }
}
