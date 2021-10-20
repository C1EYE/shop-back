package com.c1eye.server.dto;

import com.c1eye.server.core.enumeration.LoginType;
import com.c1eye.server.dto.validators.TokenPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author c1eye
 * time 2021/10/14 10:51
 */
@Getter
@Setter
public class TokenGetDTO {
    @NotBlank(message = "account不能为空")
    private String account;

    @TokenPassword(min=6,max=30,message = "{token.password}")
    private String password;

    private LoginType type;
}
