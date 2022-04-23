package mk.ukim.finki.gbook.models.exception;

public class BookAlreadyInShoppingCartException extends RuntimeException{
    public BookAlreadyInShoppingCartException(Long bookId,String username) {
        super(String.format("User with username %s already have book with id %d in shopping car",username,bookId));
    }
}
