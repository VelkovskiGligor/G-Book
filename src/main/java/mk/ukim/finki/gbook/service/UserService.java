package mk.ukim.finki.gbook.service;

import mk.ukim.finki.gbook.models.User;
import mk.ukim.finki.gbook.models.enumeration.Role;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface UserService  extends UserDetailsService {
    User register(String username, String password,String repeatPassword, String name, String surname, String number, String address, Role role);
    User login(String username,String password);
    User edit(String username, String password,String repeatPassword, String name, String surname, String number, String address, Role role);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
