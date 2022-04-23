package mk.ukim.finki.gbook.models;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Entity

@Where(clause = "deleted=false")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private String name;

    @Column(length = 6000)
    private String description;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Author author;

    private Double price;

    private String language;
    @Column(length = 50)
    private String photo;

    private Boolean deleted=false;


    public Book() {
    }

    public Book(String name, int quantity, Category category, Author author, String language,Double price,String description,String photo) {
        this.quantity = quantity;
        this.name = name;
        this.description = description;
        this.category = category;
        this.author = author;
        this.price = price;
        this.language = language;
        this.photo=photo;
        this.deleted=false;

    }
}
