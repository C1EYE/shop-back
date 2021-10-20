package com.c1eye.server.dto.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author c1eye
 * time 2021/10/14 10:58
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.TYPE})
@Constraint(validatedBy = TokenPasswordValidator.class)
public @interface TokenPassword {
    int min() default 6;

    int max() default 32;

    String message() default "不符合要求";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
