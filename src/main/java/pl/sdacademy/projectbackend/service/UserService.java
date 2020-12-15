package pl.sdacademy.projectbackend.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.exceptions.BadRequestException;
import pl.sdacademy.projectbackend.exceptions.UserAlreadyExists;
import pl.sdacademy.projectbackend.exceptions.UserNotFound;
import pl.sdacademy.projectbackend.model.Role;
import pl.sdacademy.projectbackend.model.User;
import pl.sdacademy.projectbackend.repository.UserRepository;
import pl.sdacademy.projectbackend.utilities.JwtUtil;
import pl.sdacademy.projectbackend.utilities.SecurityContextUtils;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder encoder;
    private JwtUtil jwtUtil;
    private SecurityContextUtils securityContextUtils;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder encoder, JwtUtil jwtUtil, SecurityContextUtils securityContextUtils) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.securityContextUtils = securityContextUtils;
    }

    public User updateUser(User user) {
        return userRepository.save(user);
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

    public User addUser(@Valid User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExists("Email : " + user.getEmail() + " is already taken");
        }
        if (userRepository.existsByLogin(user.getLogin())) {
            throw new UserAlreadyExists("User with login: " + user.getLogin() + " already exists");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public User findUserByEmail(String email) {
        Optional<User> userByEmail = userRepository.findUserByEmail(email);
        if (userByEmail.isEmpty()) {
            throw new UserNotFound("User with email: " + email + " doesn't exists");
        }
        return userByEmail.get();
    }

    public User findUserByToken(String token) {
        if(token == null || token.isBlank()){
            throw new BadRequestException("Token can not be empty or null");
        }
        if(!jwtUtil.validateToken(token, securityContextUtils.getCurrentUser())){
            throw new BadRequestException("Invalid token!");
        }
        return findUserByEmail(jwtUtil.extractEmail(token));
    }

    public List<User> findUserByFirstNameAndLastName(String firstName, String lastName) {
        List<User> userByFirstNameAndLastName = userRepository.findUserByFirstNameAndLastName(firstName, lastName);
        return userByFirstNameAndLastName.stream().collect(Collectors.toList());
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
