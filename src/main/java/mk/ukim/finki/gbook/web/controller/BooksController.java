package mk.ukim.finki.gbook.web.controller;

import mk.ukim.finki.gbook.models.Author;
import mk.ukim.finki.gbook.models.Book;
import mk.ukim.finki.gbook.models.Category;
import mk.ukim.finki.gbook.service.AuthorService;
import mk.ukim.finki.gbook.service.BooksService;
import mk.ukim.finki.gbook.service.CategoryService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BooksService booksService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    public BooksController(BooksService booksService, AuthorService authorService, CategoryService categoryService) {
        this.booksService = booksService;
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String getBooksPage(Model model, @RequestParam (required = false) String search) {
        List<Book> books = null;
        if (search != null && search != "") {
            books = this.booksService.findByName(search);
        } else {
            books = this.booksService.findAll();
        }

        List<Category> categories = this.categoryService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("categories", categories);
        model.addAttribute("title","Books");
        model.addAttribute("bodyContent","books");
        return "master-template";

    }

    @GetMapping("/categories/{id}")
    public String getPageByCategory(@PathVariable Long id, Model model) {
        List<Book> books = this.booksService.findByCategory(id);
        List<Category> categories = this.categoryService.findAll();
        model.addAttribute("books", books);
        model.addAttribute("categories", categories);
        model.addAttribute("title","Books");
        model.addAttribute("bodyContent","books");
        return "master-template";

    }

    @GetMapping("/add-form")
    public String addProductPage(Model model) {
        List<Category> categories = categoryService.findAll();
        List<Author> authors = authorService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("authors", authors);
        model.addAttribute("title","New book");
        model.addAttribute("bodyContent","add-book");
        return "master-template";

    }
    @Transactional
    @PostMapping("/add")
    public String saveBook(@RequestParam String name,
                           @RequestParam Integer quantity,
                           @RequestParam Long category,
                           @RequestParam Long author,
                           @RequestParam String language,
                           @RequestParam Double price,
                           @RequestParam String description,
                           @RequestParam("photo") MultipartFile mainMultipartFile) throws IOException {
        this.booksService.save(name, quantity, category, author, language, price, description,mainMultipartFile);
        return "redirect:/books";

    }
    @Transactional
    @PostMapping("/edit")
    public String editBook( @RequestParam Long id,
                            @RequestParam String name,
                           @RequestParam Integer quantity,
                           @RequestParam Long category,
                           @RequestParam Long author,
                           @RequestParam String language,
                           @RequestParam Double price,
                           @RequestParam String description,
                           @RequestParam("photo") MultipartFile mainMultipartFile) throws IOException {
        this.booksService.edit(id,name, quantity, category, author, language, price, description,mainMultipartFile);
        return "redirect:/books";

    }

    @GetMapping("edit-form/{id}")
    public String editProduct(@PathVariable Long id, Model model) {

        if (this.booksService.findById(id).isPresent()) {
            Book book = this.booksService.findById(id).get();
            List<Category> categories = this.categoryService.findAll();
            List<Author> authors = this.authorService.findAll();
            model.addAttribute("book", book);
            model.addAttribute("categories", categories);
            model.addAttribute("authors", authors);
            model.addAttribute("title","Edit book");
            model.addAttribute("bodyContent","edit-book");
            return "master-template";
        }
        return "redirect:/books?error=ProductNotFound";
    }

    @DeleteMapping("/delete/{id}")
    public String remove(@PathVariable Long id) {
        this.booksService.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/details/{id}")
    public String booksDetails(@PathVariable Long id, Model model) {
        Book book = this.booksService.findById(id).get();
        model.addAttribute("book", book);
        model.addAttribute("title",""+book.getName()+"-details");
        model.addAttribute("bodyContent","book-details");
        return "master-template";

    }
    @PostMapping("/rating/{id}")
    public String booksRating(@PathVariable Long id,@RequestParam Double rating,Model model){
        Book book=this.booksService.findById(id).get();
        //model.addAttribute("bodyContent","book-details");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        this.booksService.rate(id,username,rating);
        return "redirect:/books/details/"+id+"?hasRating=true";
    }


}
