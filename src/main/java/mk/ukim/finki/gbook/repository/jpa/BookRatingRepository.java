package mk.ukim.finki.gbook.repository.jpa;

import mk.ukim.finki.gbook.models.Book;
import mk.ukim.finki.gbook.models.BookRating;
import mk.ukim.finki.gbook.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRatingRepository extends JpaRepository<BookRating,Long> {
    Optional<BookRating> findByUserAndAndBook(User user, Book book);
}
