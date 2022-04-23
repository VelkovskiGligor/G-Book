package mk.ukim.finki.gbook.service;

import mk.ukim.finki.gbook.models.Book;
import mk.ukim.finki.gbook.models.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    List<Book> listAllBooksInShoppingCart(Long cartId);
    ShoppingCart getActiveShoppingCart (String username);
    ShoppingCart addProductToShoppingCart(String username,Long bookId);
    void deleteProductInShoppingCart(String username,Long id);
    ShoppingCart setFinish(Long id);
    void checkSum(List<Book> books,Long id);


}
