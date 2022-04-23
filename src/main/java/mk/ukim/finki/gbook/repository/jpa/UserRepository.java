package mk.ukim.finki.gbook.repository.jpa;

import mk.ukim.finki.gbook.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndPassword(String username,String password);

}
