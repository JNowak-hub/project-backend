package pl.sdacademy.projectbackend.validaiton.customvalidators.emailavailable;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailAvailableValidator.class)
@Target( { ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailAvailable {
    String message() default "Email already taken";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
