package ru.practicum.shareit.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmailRFC2822ConstraintValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EmailRFC2822 {
    String message() default "Invalid email address. Mail does not match rfc2822 pattern";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}