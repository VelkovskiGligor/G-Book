package mk.ukim.finki.gbook.repository.jpa;

import mk.ukim.finki.gbook.models.Author;
import mk.ukim.finki.gbook.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book,Long> {
        void deleteByName(String name);
        List<Book> findByCategory_Id(Long id);
        List<Book> findAllByNameContaining(String name);
        void deleteByNameAndAuthor(String name, Author author);
        Book findByNameAndAuthor(String name,Author author);

}
