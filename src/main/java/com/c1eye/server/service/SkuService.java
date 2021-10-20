package com.c1eye.server.service;

import com.c1eye.server.model.Sku;
import com.c1eye.server.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author c1eye
 * time 2021/10/20 15:23
 */
@Service
public class SkuService {
    @Autowired
    private SkuRepository skuRepository;

    public List<Sku> getSkuListByIds(List<Long> ids){
        return skuRepository.findAllByIdIn(ids);
    }
}
