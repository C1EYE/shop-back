package com.c1eye.server.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author c1eye
 * time 2021/9/29 15:56
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordEqual {
    int min() default 4;

    int max() default 6;

    String message() default "password are not equal";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
