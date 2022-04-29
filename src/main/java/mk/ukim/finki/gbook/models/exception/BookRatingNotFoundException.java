package mk.ukim.finki.gbook.models.exception;

public class BookRatingNotFoundException extends RuntimeException{
    public BookRatingNotFoundException(String username,Long id) {
        super( String.format("Book rating not found for User with username %s and Book with id %d",username,id));
    }
}
