package com.c1eye.server.util;

import com.c1eye.server.exception.http.ServerErrorException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import java.util.List;
import java.util.Map;

/**
 * @author c1eye
 * time 2021/10/11 11:09
 */
public class ListAndJson implements AttributeConverter<List<Object>,String> {
    @Autowired
    private ObjectMapper mapper;

    @Override
    public String convertToDatabaseColumn(List<Object> objects) {
        try {
            return mapper.writeValueAsString(objects);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }

    @Override
    public List<Object> convertToEntityAttribute(String s) {
        try{
            if(s == null){
                return null;
            }
            List<Object> list = mapper.readValue(s, List.class);
            return list;
        }catch(Exception e){
            e.printStackTrace();
            throw new ServerErrorException(9999);
        }
    }
}
