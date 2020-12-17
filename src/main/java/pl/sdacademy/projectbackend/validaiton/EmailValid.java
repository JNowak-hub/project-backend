package pl.sdacademy.projectbackend.validaiton;

import pl.sdacademy.projectbackend.validaiton.customvalidators.emailavailable.EmailAvailable;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.*;

@Email(message = "Email must be valid")
@NotEmpty
@NotNull(message = "Email can not be null")
@EmailAvailable
@Target( {ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Documented
public @interface EmailValid {
    String message() default "{com.mycompany.constraints.validlicenseplate}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
