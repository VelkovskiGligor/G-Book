package mk.ukim.finki.gbook.models.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(Long id) {
        super(String.format("Author with id %d not found.",id));
    }
}
