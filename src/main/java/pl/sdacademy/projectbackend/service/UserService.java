package pl.sdacademy.projectbackend.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.Role;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.UserRepository;

import javax.validation.Valid;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public User findUserById(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotFound("User with id: " + id + " doesn't exists");
        }

        return optionalUser.get();
    }

    public void deleteUserById(Long id) {
        User user = findUserById(id);
        userRepository.delete(user);
    }

    @Valid
    public User addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findUserByLogin(login);
        if (optionalUser.isEmpty()) {
            throw new UserNotFound("User with login: " + login + " doesn't exists");
        }
        return optionalUser.get();
    }
}
