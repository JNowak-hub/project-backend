package pl.sdacademy.projectbackend.validaiton.customvalidators.loginavailbe;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sdacademy.projectbackend.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginAvailableValidator implements ConstraintValidator<LoginAvailable, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if(userRepository == null){
            return true;
        }
        return !userRepository.existsByLogin(login);
    }
}
