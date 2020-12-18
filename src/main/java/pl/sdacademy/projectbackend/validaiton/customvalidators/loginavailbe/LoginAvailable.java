package pl.sdacademy.projectbackend.validaiton.customvalidators.loginavailbe;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LoginAvailableValidator.class)
@Target( { ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginAvailable {
    String message() default "Login already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
