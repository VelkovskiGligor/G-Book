package mk.ukim.finki.gbook.models.exception;

public class PasswordDoNotMatchException extends RuntimeException{
    public PasswordDoNotMatchException() {
        super("Password do not match exception");
    }
}
