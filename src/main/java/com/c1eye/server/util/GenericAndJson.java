package com.c1eye.server.util;

import com.c1eye.server.exception.http.ServerErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author c1eye
 * time 2021/10/11 22:10
 */
public class GenericAndJson {

    private static ObjectMapper mapper;

    @Autowired
    public void setMapper(ObjectMapper mapper) {
        GenericAndJson.mapper = mapper;
    }

    public static <T> String objectToJson(T o) {
        try {
            return GenericAndJson.mapper.writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    public static <T> T jsonToObject(String s, Class<T> tClass) {
        if (s == null) {
            return null;
        }
        try {
            T o = GenericAndJson.mapper.readValue(s, tClass);
            return o;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    public static <T> List<T> jsonToList(String s) {
        if (s == null) {
            return null;
        }
        try {
            List<T> tList = GenericAndJson.mapper.readValue(s, new TypeReference<List<T>>() {});
            return tList;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }


}
