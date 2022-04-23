package mk.ukim.finki.gbook.service;

import mk.ukim.finki.gbook.models.Author;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<Author> findAll();
    Optional<Author> findById(Long id);
     Optional<Author> save(String name, String surname, String desc, String from, MultipartFile multipartFile) throws IOException;
}
