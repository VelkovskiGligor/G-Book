package mk.ukim.finki.gbook.web.controller;

import mk.ukim.finki.gbook.models.Author;
import mk.ukim.finki.gbook.service.AuthorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorService authorService;

    public AuthorsController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public String getAuthorsPage(Model model){
        List<Author> authors=authorService.findAll();
        model.addAttribute("authors",authors);
        model.addAttribute("title","Authors");
        model.addAttribute("bodyContent","authors");
        return "master-template";
    }

    @GetMapping("/add-form")
    public String addNewAuthor(Model model){
        model.addAttribute("title","Add Author");
        model.addAttribute("bodyContent","add-author");
        return "master-template";

    }

    @PostMapping("/add")
    public String  submitAuthor(@RequestParam String name,
                                @RequestParam String surname,
                                @RequestParam String description,
                                @RequestParam String from,
                                @RequestParam("photo") MultipartFile mainMultipartFile) throws IOException {
        this.authorService.save(name, surname, description,from,mainMultipartFile);
        return "redirect:/authors";

    }
    @GetMapping("/details/{id}")
    public String getDetailsPage(@PathVariable Long id,Model model){
        Author author=authorService.findById(id).get();
        model.addAttribute("author",author);
        model.addAttribute("title","Author details");
        model.addAttribute("bodyContent","author-details");
        return "master-template";
    }




}
