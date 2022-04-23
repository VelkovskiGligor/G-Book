package mk.ukim.finki.gbook.models;

import lombok.Data;
import mk.ukim.finki.gbook.models.enumeration.ShoppingCartStatus;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@SQLDelete(sql = "UPDATE shopping_cart SET deleted = true WHERE id=?")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime dataCreated;

    @ManyToOne
    private User user;

    @ManyToMany(cascade = CascadeType.REMOVE)

    private List<Book> books;

    @Enumerated(EnumType.STRING)
    private ShoppingCartStatus status;

    private Double totalPrice;
    private Boolean update;
    public ShoppingCart() {
    }

    public ShoppingCart(User user) {
        totalPrice=0.00;
        this.user = user;
        this.dataCreated = LocalDateTime.now();
        this.books = new ArrayList<>();
        this.status = ShoppingCartStatus.CREATED;
        this.update=false;
    }
}
