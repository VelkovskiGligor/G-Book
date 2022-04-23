package mk.ukim.finki.gbook.models.exception;

public class InvalidUserCredentialsException extends RuntimeException{
    public InvalidUserCredentialsException() {
        super("InvalidUserCredentialsException");
    }
}
