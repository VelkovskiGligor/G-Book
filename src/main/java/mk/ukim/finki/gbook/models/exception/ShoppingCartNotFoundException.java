package mk.ukim.finki.gbook.models.exception;

public class ShoppingCartNotFoundException extends RuntimeException{
    public ShoppingCartNotFoundException(Long cartId) {
        super(String.format("Shopping cart with id %d was not found",cartId));
    }
}
