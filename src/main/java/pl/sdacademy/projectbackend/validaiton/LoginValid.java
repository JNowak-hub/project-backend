package pl.sdacademy.projectbackend.validaiton;

import org.hibernate.validator.constraints.Length;
import pl.sdacademy.projectbackend.validaiton.customvalidators.loginavailbe.LoginAvailable;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Length(min = 3, message = "login can not be shorter than 3 characters")
@NotNull(message = "Login can not be null")
@LoginAvailable
@Target( {ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface LoginValid {
    String message() default "{com.mycompany.constraints.validlicenseplate}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
