package pl.sdacademy.projectbackend.validaiton.customvalidators.emailavailable;

import pl.sdacademy.projectbackend.repository.UserRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailAvailableValidator implements ConstraintValidator<EmailAvailable,String> {

    private UserRepository userRepository;

    public EmailAvailableValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByEmail(email);
    }
}
