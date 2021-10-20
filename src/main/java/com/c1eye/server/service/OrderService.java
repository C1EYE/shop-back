package com.c1eye.server.service;

import com.c1eye.server.dto.OrderDTO;
import com.c1eye.server.dto.SkuInfoDTO;
import com.c1eye.server.exception.http.ParameterException;
import com.c1eye.server.model.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author c1eye
 * time 2021/10/20 15:18
 */
@Service
public class OrderService {

    @Autowired
    private SkuService skuService;
    public void isOk(Long uid, OrderDTO orderDTO){
        if(orderDTO.getFinalTotalPrice().compareTo(new BigDecimal("0")) <= 0){
            throw new ParameterException(50011);
        }

        List<Long> skuIdList = orderDTO.getSkuInfoList().stream().map(SkuInfoDTO::getId).collect(Collectors.toList());
        List<Sku> skuList = skuService.getSkuListByIds(skuIdList);

    }
}
