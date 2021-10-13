package com.c1eye.server.vo;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * @author c1eye
 * time 2021/10/10 11:04
 */
public class PagingDozer<T,K> extends Paging<T>{
    public PagingDozer(Page<T> pageT,Class<K> classK){
        this.initPageParameters(pageT);
        List<T> tList = pageT.getContent();
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();
        List<K> voList = new ArrayList<>();
        tList.forEach(e->{
            K k = mapper.map(e, classK);
            voList.add(k);
        });
        this.setItems(voList);
    }
}
