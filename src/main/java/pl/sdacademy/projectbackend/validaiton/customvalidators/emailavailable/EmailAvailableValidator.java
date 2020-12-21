package pl.sdacademy.projectbackend.validaiton.customvalidators.emailavailable;

import org.springframework.beans.factory.annotation.Autowired;
import pl.sdacademy.projectbackend.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailAvailableValidator implements ConstraintValidator<EmailAvailable,String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        if(userRepository == null){
            return true;
        }
        return !userRepository.existsByEmail(email);
    }
}
