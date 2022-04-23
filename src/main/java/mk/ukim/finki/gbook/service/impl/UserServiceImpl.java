package mk.ukim.finki.gbook.service.impl;

import mk.ukim.finki.gbook.models.User;
import mk.ukim.finki.gbook.models.enumeration.Role;
import mk.ukim.finki.gbook.models.exception.InvalidArgumentException;
import mk.ukim.finki.gbook.models.exception.InvalidUserCredentialsException;
import mk.ukim.finki.gbook.models.exception.PasswordDoNotMatchException;
import mk.ukim.finki.gbook.models.exception.UsernameAlreadyExistsException;
import mk.ukim.finki.gbook.repository.jpa.UserRepository;
import mk.ukim.finki.gbook.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(String username,String name,String surname,String address,String number,String  password,String repeatedPassword,Role role) {
        if(username==null || username.isEmpty() || password==null || password.isEmpty()){
            throw new InvalidArgumentException();
        }
        if(!password.equals(repeatedPassword)){
            throw  new PasswordDoNotMatchException();
        }
        if(this.userRepository.findByUsername(username).isPresent()){
            throw  new UsernameAlreadyExistsException(username);
        }
        User user=new User(username,passwordEncoder.encode(password),name,surname,number,address,role);
        return this.userRepository.save(user);

    }

    @Override
    public User login(String username, String password) {
        if(username==null || username.isEmpty() || password==null || password.isEmpty()){
            throw  new InvalidArgumentException();
        }

        return this.userRepository.findByUsernameAndPassword(username,password)
                .orElseThrow(InvalidUserCredentialsException::new);
    }

    @Override
    public User edit(String username, String password, String repeatPassword, String name, String surname, String number, String address, Role role) {
        if(username==null || username.isEmpty() || password==null || password.isEmpty()){
            throw new InvalidArgumentException();
        }
        if(!password.equals(repeatPassword)){
            throw  new PasswordDoNotMatchException();
        }

        User user=new User(username,passwordEncoder.encode(password),name,surname,number,address,role);
        return this.userRepository.save(user);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return  this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
