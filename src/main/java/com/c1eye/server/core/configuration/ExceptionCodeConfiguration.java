package com.c1eye.server.core.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * author c1eye
 * time 2021/9/28 15:08
 * @author c1eye
 */
@ConfigurationProperties(prefix = "c1eye")
@PropertySource("classpath:config/exception-code.properties")
@Component
@Data
public class ExceptionCodeConfiguration {

    private Map<Integer, String> codes = new HashMap<>();

    public String getMessage(int code){
        String message = codes.get(code);
        return message;
    }


}
