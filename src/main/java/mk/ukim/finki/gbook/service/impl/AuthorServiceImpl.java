package mk.ukim.finki.gbook.service.impl;

import mk.ukim.finki.gbook.models.Author;
import mk.ukim.finki.gbook.models.Book;
import mk.ukim.finki.gbook.models.exception.AuthorNotFoundException;
import mk.ukim.finki.gbook.repository.jpa.AuthorRepository;
import mk.ukim.finki.gbook.service.AuthorService;
import mk.ukim.finki.gbook.web.controller.FileUploadUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final  AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<Author> findAll() {
        return  this.authorRepository.findAll();
    }

    @Override
    public Optional<Author> findById(Long id) {
        return Optional.of( this.authorRepository.findById(id).orElseThrow(()-> new AuthorNotFoundException(id)));
    }
    @Transactional
    @Override
    public Optional<Author> save(String name, String surname, String desc, String from, MultipartFile authorPhoto) throws IOException {


        String fileName = StringUtils.cleanPath(authorPhoto.getOriginalFilename());
        Author author=new Author(name,surname,desc,from,fileName);
        Author auth = this.authorRepository.save(author);
        String uploadDir = "./src/main/resources/static/img/authors" + auth.getId();
        FileUploadUtil.saveFile(uploadDir,authorPhoto,fileName);
        return  Optional.of( this.authorRepository.save(author));

    }

}
