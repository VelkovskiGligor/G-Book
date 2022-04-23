package mk.ukim.finki.gbook.repository.jpa;

import mk.ukim.finki.gbook.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author,Long> {
        void deleteById(Long id);
}
