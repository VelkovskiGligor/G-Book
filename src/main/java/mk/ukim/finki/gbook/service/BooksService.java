package mk.ukim.finki.gbook.service;

import mk.ukim.finki.gbook.models.Book;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BooksService {
    List<Book> findAll();
    Optional<Book> findById(Long id);
     Optional<Book> save(String name, Integer quantity, Long categoryId, Long authorId, String  language, Double price, String description, MultipartFile mainMultipartFile) throws IOException;
    void deleteById(Long id);
    List<Book> findByCategory(Long id);
    List<Book> findByName(String name);
    Optional<Book> edit(Long id,String name, Integer quantity, Long categoryId, Long authorId, String  language, Double price, String description, MultipartFile mainMultipartFile) throws IOException;
}
