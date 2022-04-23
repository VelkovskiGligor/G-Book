package mk.ukim.finki.gbook.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Author {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String name;
    private String surname;
    @Column(length = 4000)
    private String aboutActor;

    private String country;
    @Column(length = 50)
    private String authorPhoto;

    public Author(String name, String surname, String aboutActor, String country,String authorPhoto) {
        this.name = name;
        this.surname = surname;
        this.aboutActor = aboutActor;
        this.country = country;
        this.authorPhoto=authorPhoto;
    }

    public Author() {
    }
    public String fullName(){
        return this.name+" "+this.surname;
    }
}
