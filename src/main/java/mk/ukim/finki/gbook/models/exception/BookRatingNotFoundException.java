package mk.ukim.finki.gbook.models.exception;

import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class BookRatingNotFoundException extends RuntimeException{
    public BookRatingNotFoundException(String username,Long id) {
        super( String.format("Book rating not found for User with username %s and Book with id %d",username,id));
    }
}
