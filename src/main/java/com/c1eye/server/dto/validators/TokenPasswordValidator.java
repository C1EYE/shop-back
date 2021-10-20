package com.c1eye.server.dto.validators;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author c1eye
 * time 2021/10/14 11:13
 */
public class TokenPasswordValidator implements ConstraintValidator<TokenPassword,String> {
    private Integer min;
    private Integer max;


    @Override
    public void initialize(TokenPassword constraintAnnotation) {
        this.max = constraintAnnotation.max();
        this.min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(StringUtils.isEmpty(s)){
            return true;
        }
        return s.length() >= this.min && s.length() <= this.max;
    }
}
