package mk.ukim.finki.gbook.repository.jpa;

import mk.ukim.finki.gbook.models.ShoppingCart;
import mk.ukim.finki.gbook.models.User;
import mk.ukim.finki.gbook.models.enumeration.ShoppingCartStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
   Optional <ShoppingCart> findByUserAndStatus(User user, ShoppingCartStatus status);


}
