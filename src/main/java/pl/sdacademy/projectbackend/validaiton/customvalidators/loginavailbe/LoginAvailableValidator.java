package pl.sdacademy.projectbackend.validaiton.customvalidators.loginavailbe;

import pl.sdacademy.projectbackend.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginAvailableValidator implements ConstraintValidator<LoginAvailable, String> {

    private UserRepository userRepository;

    public LoginAvailableValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if(userRepository == null){
            return true;
        }
        return !userRepository.existsByLogin(login);
    }
}
