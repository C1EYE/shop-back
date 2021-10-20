package com.c1eye.server.dto;

import com.c1eye.server.dto.validators.PasswordEqual;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * author c1eye
 * time 2021/9/29 11:04
 * @author c1eye
 */

@Builder
@Getter
@PasswordEqual
public class PersonDTO {
    @NonNull
    private String name;
    @Length(min = 0,max = 100,message = "ALG")
    private Integer age;

}
