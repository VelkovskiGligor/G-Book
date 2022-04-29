package mk.ukim.finki.gbook.service.impl;

import mk.ukim.finki.gbook.models.*;
import mk.ukim.finki.gbook.models.exception.AuthorNotFoundException;
import mk.ukim.finki.gbook.models.exception.BookNotFoundException;
import mk.ukim.finki.gbook.models.exception.BookRatingNotFoundException;
import mk.ukim.finki.gbook.models.exception.CategoryNotFoundException;
import mk.ukim.finki.gbook.repository.jpa.*;
import mk.ukim.finki.gbook.service.BooksService;
import mk.ukim.finki.gbook.web.controller.FileUploadUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BooksService {

    private final BooksRepository booksRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final BookRatingRepository bookRatingRepository;

    public BookServiceImpl(BooksRepository booksRepository, AuthorRepository authorRepository, CategoryRepository categoryRepository, UserRepository userRepository, BookRatingRepository bookRatingRepository) {
        this.booksRepository = booksRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.bookRatingRepository = bookRatingRepository;

    }

    @Override
    public List<Book> findAll() {
        return this.booksRepository.findAll();
    }

    @Override
    public List<Book> findByName(String name) {
        return this.booksRepository.findAllByNameContaining(name);
    }

    @Override
    public Optional<Book> edit(Long id,String name, Integer quantity, Long categoryId, Long authorId, String language, Double price, String description, MultipartFile mainMultipartFile) throws IOException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));
       Book book= this.booksRepository.findById(id)
               .orElseThrow(()->new BookNotFoundException(id));
       book.setName(name);
        book.setQuantity(quantity);
        book.setCategory(category);
        book.setAuthor(author);
        book.setDescription(description);
        book.setLanguage(language);
        book.setPrice(price);
        if(!mainMultipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
            book.setPhoto(fileName);
            String uploadDir = "./src/main/resources/static/img/books" + book.getId();
            FileUploadUtil.saveFile(uploadDir,mainMultipartFile,fileName);
        }

        this.booksRepository.save(book);


        return Optional.of(book);
    }

    @Override
    public List<Book> findByCategory(Long id) {
        return this.booksRepository.findByCategory_Id(id);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.of(this.booksRepository.findById(id)).orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional
    @Override
    public Optional<Book> save(String name, Integer quantity, Long categoryId, Long authorId, String language, Double price, String description, MultipartFile mainMultipartFile) throws IOException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException(authorId));
        this.booksRepository.deleteByNameAndAuthor(name,author);


        String fileName = StringUtils.cleanPath(mainMultipartFile.getOriginalFilename());
        Book newBook = new Book(name, quantity, category, author, language, price, description, fileName);
        Book book = this.booksRepository.save(newBook);
        String uploadDir = "./src/main/resources/static/img/books" + book.getId();
        FileUploadUtil.saveFile(uploadDir,mainMultipartFile,fileName);
        this.booksRepository.save(book);

        return Optional.of(book);
    }
@Transactional
    @Override
    public void deleteById(Long id) {
        Book book=booksRepository.findById(id).get();
        book.setDeleted(true);
      this.booksRepository.save(book);

    }

    @Override
    public void rate(Long bookId, String username, Double rate) {
        User user=this.userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException(username));
        Book book=this.booksRepository.findById(bookId)
                .orElseThrow(()->new BookNotFoundException(bookId));
        if(bookRatingRepository.findByUserAndAndBook(user,book).isPresent()){
            BookRating bookRating= this.bookRatingRepository.findByUserAndAndBook(user,book)
                    .orElseThrow(()->new BookRatingNotFoundException(username,bookId));
            Double oldRate=bookRating.getRate();
           // Integer numRating=book.getNumRate();
            book.setRate((book.getRate()-oldRate+rate));
            bookRating.setRate(rate);
            this.bookRatingRepository.save(bookRating);
            this.booksRepository.save(book);

        }else{
          //  BookRating bookRating=
            this.bookRatingRepository.save(new BookRating(user,book,rate));
            Integer numRating=book.getNumRate();
            book.setNumRate(numRating+1);
            book.setRate((book.getRate()+rate));
            this.booksRepository.save(book);

        }


    }

}
