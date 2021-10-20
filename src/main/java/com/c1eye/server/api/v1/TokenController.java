package com.c1eye.server.api.v1;

import com.c1eye.server.dto.TokenDTO;
import com.c1eye.server.dto.TokenGetDTO;
import com.c1eye.server.exception.http.NotFoundException;
import com.c1eye.server.exception.http.ParameterException;
import com.c1eye.server.service.WxAuthenticationService;
import com.c1eye.server.util.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author c1eye
 * time 2021/10/14 10:49
 */
@RestController
@RequestMapping("/token")
public class TokenController {

    @Autowired
    WxAuthenticationService wxAuthenticationService;

    @PostMapping("")
    public Map<String, String> getToken(@RequestBody @Validated TokenGetDTO userData) {
        HashMap<String, String> map = new HashMap<>();
        String token = null;
        switch (userData.getType()) {
            case USER_WX:
                token = wxAuthenticationService.code2Session(userData.getAccount());
                break;
            case USER_Email:
                break;
            default:
                throw new NotFoundException(10003);
        }
        map.put("token", token);
        return map;
    }

    @PostMapping("/verify")
    public Map<String, Boolean> verify(@RequestBody TokenDTO token) {
        Map<String, Boolean> map = new HashMap<>();
        Boolean valid = JwtToken.verifyToken(token.getToken());
        map.put("is_valid", valid);
        return map;
    }

}
