package com.c1eye.server.service;

import com.c1eye.server.exception.http.ParameterException;
import com.c1eye.server.model.User;
import com.c1eye.server.repository.UserRepository;
import com.c1eye.server.util.JwtToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author c1eye
 * time 2021/10/14 12:10
 */
@Service
public class WxAuthenticationService {

    @Autowired
    private ObjectMapper mapper;
    @Value("${wx.code2session}")
    private String code2SessionUrl;
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.appsecret}")
    private String appsecret;

    @Autowired
    private UserRepository userRepository;
    private Map<String, Object> session;

    public String code2Session(String code) {
        String url = MessageFormat.format(code2SessionUrl, this.appid, this.appsecret, code);
        RestTemplate rest = new RestTemplate();
        String sessionText = rest.getForObject(url, String.class);
        session = new HashMap<>();
        try {
            session = mapper.readValue(sessionText, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return this.registerUser(session);
    }

    private String registerUser(Map<String, Object> session) {
        String openid = (String) session.get("openid");
        if (openid == null) {
            throw new ParameterException(20004);
        }
        Optional<User> userOptional = userRepository.findByOpenid(openid);
        if (userOptional.isPresent()) {
            //scope 用来建立数字等级进行访问控制
            return JwtToken.makeToken(userOptional.get().getId());
        }
        User user = User.builder().openid(openid).build();
        userRepository.save(user);
        Long uid = user.getId();
        return JwtToken.makeToken(uid);
    }
}
