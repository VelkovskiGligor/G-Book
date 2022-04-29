package mk.ukim.finki.gbook.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class BookRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;
    @ManyToOne
    private Book book;

    private Double rate;

    public BookRating(){}

    public BookRating(User user, Book book, Double rate) {
        this.user = user;
        this.book = book;
        this.rate = rate;
    }
}
