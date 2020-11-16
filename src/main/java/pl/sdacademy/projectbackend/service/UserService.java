package pl.sdacademy.projectbackend.service;

import org.springframework.stereotype.Service;
import pl.sdacademy.projectbackend.repository.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



}
