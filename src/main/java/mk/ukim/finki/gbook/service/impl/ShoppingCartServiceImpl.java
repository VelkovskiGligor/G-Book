package mk.ukim.finki.gbook.service.impl;

import mk.ukim.finki.gbook.models.Book;
import mk.ukim.finki.gbook.models.ShoppingCart;
import mk.ukim.finki.gbook.models.User;
import mk.ukim.finki.gbook.models.enumeration.ShoppingCartStatus;
import mk.ukim.finki.gbook.models.exception.BookAlreadyInShoppingCartException;
import mk.ukim.finki.gbook.models.exception.BookNotFoundException;
import mk.ukim.finki.gbook.models.exception.EmptyShoppingCart;
import mk.ukim.finki.gbook.models.exception.ShoppingCartNotFoundException;
import mk.ukim.finki.gbook.repository.jpa.BooksRepository;
import mk.ukim.finki.gbook.repository.jpa.ShoppingCartRepository;
import mk.ukim.finki.gbook.repository.jpa.UserRepository;
import mk.ukim.finki.gbook.service.ShoppingCartService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
  private final  ShoppingCartRepository shoppingCartRepository;
  private final UserRepository userRepository;
  private  final BooksRepository booksRepository;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserRepository userRepository, BooksRepository booksRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userRepository = userRepository;
        this.booksRepository = booksRepository;
    }
    @Transactional
    @Override
    public List<Book> listAllBooksInShoppingCart(Long cartId) {
        if(!this.shoppingCartRepository.findById(cartId).isPresent()){
            throw  new ShoppingCartNotFoundException(cartId);
        }
        ShoppingCart shoppingCart=this.shoppingCartRepository.findById(cartId)
                .orElseThrow(()->new ShoppingCartNotFoundException(cartId));

        return this.shoppingCartRepository.findById(cartId).get().getBooks();
    }

    @Override
    public ShoppingCart getActiveShoppingCart(String username) {
        User user=this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return this.shoppingCartRepository.findByUserAndStatus(user, ShoppingCartStatus.CREATED)
                .orElseGet(()->{
                    ShoppingCart cart=new ShoppingCart(user);
                    return this.shoppingCartRepository.save(cart);
                });
    }

    @Override
    public ShoppingCart addProductToShoppingCart(String username, Long bookId) {
        ShoppingCart shoppingCart=getActiveShoppingCart(username);
        Book book=this.booksRepository.findById(bookId).orElseThrow(()-> new BookNotFoundException(bookId));
        if( shoppingCart.getBooks().stream().filter(r-> r.getId().equals(bookId))
                .collect(Collectors.toList()).size()>0){
            throw new BookAlreadyInShoppingCartException(bookId,username);
        }
        shoppingCart.setTotalPrice(shoppingCart.getTotalPrice()+book.getPrice());
         shoppingCart.getBooks().add(book);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void deleteProductInShoppingCart(String username, Long id) {
        ShoppingCart shoppingCart=getActiveShoppingCart(username);
        Optional<Book> curentBook= shoppingCart.getBooks().stream().filter(r->r.getId().equals(id)).findFirst();
        Double minus=curentBook.get().getPrice();
        shoppingCart.setTotalPrice(shoppingCart.getTotalPrice()-minus);
        curentBook.get().setQuantity(curentBook.get().getQuantity()+1);
        shoppingCart.getBooks().removeIf(r->r.getId().equals(id));
        this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart setFinish(Long id) {
        ShoppingCart shoppingCart=this.shoppingCartRepository.getById(id);
        if(shoppingCart.getBooks().size() == 0){
            throw  new EmptyShoppingCart("Your Shopping cart empty");
        }
        shoppingCart.setStatus(ShoppingCartStatus.FINISHED);
       return shoppingCartRepository.save(shoppingCart);
    }
        // ZA PROVERKA
    @Override
    public void checkSum(List<Book> books,Long id) {
        double sum=0;
        ShoppingCart shoppingCart=this.shoppingCartRepository.findById(id).orElseThrow(()->new ShoppingCartNotFoundException(id));
        for (Book book: books){
                if(book.getDeleted()== false){
                    sum+= book.getPrice();
                }
        }
        if(shoppingCart.getTotalPrice() !=sum){
                shoppingCart.setTotalPrice(sum);
                this.shoppingCartRepository.save(shoppingCart);
        }
    }
}
